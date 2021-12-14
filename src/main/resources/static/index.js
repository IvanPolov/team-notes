angular.module('app', []).controller('indexController', function ($rootScope, $scope, $http, $interval, $compile) {
    const contextPath = 'http://localhost:8180/team-notes/api/v1';

    $scope.invitedUser = null;
    $scope.foundUser = null;
    $scope.isFounded = false;
    $scope.Users = [];

    $scope.Colors = [
        {colorHex: '#e06666', description: 'red'},
        {colorHex: '#f6b26b', description: 'orange'},
        {colorHex: '#ffd966', description: 'yellow'},
        {colorHex: '#93c47d', description: 'green'},
        {colorHex: '#76a5af', description: 'cyan'},
        {colorHex: '#6d9eeb', description: 'blue'},
        {colorHex: '#8e7cc3', description: 'purple'},
        {colorHex: '#c27ba0', description: 'magenta'}];

    $scope.SortProperties = ['priority', 'color', 'isFavorite', 'createDate', 'lastModifiedDate'];
    $scope.propertyName = 'priority';
    $scope.reverse = true;

    $scope.acronym = function (sentence, size) {
        if (sentence != null) {
            console.log('acronym: ' + sentence + 'size: ' + size);
            let acro = sentence.split(' ').map(x => x[0]).join('').slice(0, size).toUpperCase();
            console.log(acro);
            return acro;
        }
        return '';
    }


    $scope.saveNote = function () {
        $scope.newNote.boardId = $scope.currentBoard.id;
        console.log($scope.newNote)
        $http.post(contextPath + '/note', $scope.newNote)
            .then(function (resp) {
                $scope.fillBoardWithNotes($scope.currentBoard);
                // $scope.newNote = null;
                document.querySelector('#closeNoteButton').click();
            })

    }

    $scope.setNote = function (note) {
        $scope.newNote = angular.copy(note)
    }

    $scope.updateNote = function (note) {
        console.log($scope.currentBoard)
        note.boardId = $scope.currentBoard.id
        console.log(note)
        $http.put(contextPath + '/note', note)
            .then(function (resp) {
                $scope.fillBoardWithNotes($scope.currentBoard);
            })

    }
    $scope.deleteNote = function (id) {
        console.log('note id: ' + id)
        $http.delete(contextPath + '/note/' + id)
            .then(function (resp) {
                $scope.fillBoardWithNotes($scope.currentBoard);
            })

    };

    // $scope.isDataReady = function (dataForm, boardName){
    //     return !(dataForm.$pristine || dataForm.$invalid || !dataForm.name);
    //
    // }
    // $scope.invitedUser = $scope.user;
    // $scope.invitedUser.email = 'some@email.com';
    // $scope.updateUsers = function (){
    // console.log($scope.invitedUser)
    // $http.get(contextPath+'/user/' + $scope.invitedUser.email)
    //     .then(function (resp){
    //
    // });
    // }

    //get user by email
    $scope.findUser = function (email) {
        if (email != null && email !== $scope.user.email) {
            console.log(email);
            $http.get(contextPath + '/user/' + email)
                .then(function (resp) {
                    console.log('find user data')
                    console.log(resp.data)
                    if (email === resp.data.email && !$scope.Users.find(x => x.email === resp.data.email)) {
                        $scope.foundUser = resp.data;
                        $scope.isFounded = true;
                    } else {
                        $scope.isFounded = false;
                        console.log('email not found or already added')
                    }
                    // $scope.addUser();
                })
        } else $scope.isFounded = false;
    }

    //get current session user
    $scope.getUser = function () {
        $http.get(contextPath + '/user')
            .then(function (resp) {
                $scope.user = resp.data
                console.log($scope.user.id)
                $scope.getBoards();
                $scope.isVerified = resp.data.isVerified;
            })
    }

    $scope.flag = false;
    $scope.checkUser = function () {

        if (!$scope.isVerified) {
            $http.get(contextPath + '/user/check')
                .then(function (resp1) {
                    $scope.isVerified = resp1.data.isVerified;
                    console.log('+++++++++++' + resp1.data.isVerified)
                    $scope.flag = true;
                }, function error(resp2) {
                    setTimeout(
                        () => {
                            location.replace('promo.html');
                        },
                        10
                    );
                })
        } else {
            if ($scope.flag) {
                location.reload();
                $scope.flag = false;
            }
        }
    }

    $interval($scope.checkUser, 5000);

//add user to board
    $scope.addUser = function () {
        console.log('add user func')
        // PUT http://localhost:8180/api/v1/board/{{boardId}}/addUser
        $http.get(contextPath + '/board/' + $scope.currentBoard.id + '/addUser/' + $scope.foundUser.id)
            .then(function () {
                console.log('add user func resp')
                $scope.invitedUser.email = null;
                console.log($scope.foundUser)
                $scope.Users.push($scope.foundUser);
                $scope.isFounded = false;
                // $scope.connect();
            })
    }
//remove user from the board
    $scope.removeUser = function (user) {
        $http.delete(contextPath + '/board/' + $scope.currentBoard.id + '/removeUser/' + user.id)
            .then(function () {
                $scope.Users.splice($scope.Users.indexOf(user), 1);
            })
    }

    if (!$scope.user) {
        $scope.getUser();
    }
    $scope.getBoardUsers = function () {

        // GET http://localhost:8180/api/v1/user/board/{{boardId}}
        $http.get(contextPath + "/user/board/" + $scope.currentBoard.id)
            .then(function (resp) {
                $scope.Users = resp.data;
                console.log('users of board')
                console.log($scope.Users)
                $scope.getRoles();
            })
    }
    $scope.fillBoardWithNotes = function (currentBoard) {
        disconnect();
        console.log('board id: ' + currentBoard.id)
        $scope.currentBoard = currentBoard;
        $scope.getBoardUsers();
        $http({
            url: contextPath + '/board/' + currentBoard.id + '/notes',
            method: 'GET'
        }).then(function (response) {
            $scope.Notes = response.data;
            connect();
        });
    };

    $scope.getBoards = function () {
        console.log('get all boards function');
        $http.get(contextPath + '/board/user/' + $scope.user.id)
            .then(function (response) {
                $scope.Boards = response.data;
                console.log($scope.Boards);
            });
    }
    $scope.updateBoard = function (board) {
        $http.put(contextPath + '/board', board)
            .then(function (resp) {
                $scope.getBoards();
            })

    }

    $scope.saveBoard = function () {
        $scope.newBoard.ownerId = $scope.user.id;
        console.log($scope.newBoard)
        $http.post(contextPath + '/board', $scope.newBoard)
            .then(function (resp) {
                $scope.getBoards();
                $scope.newBoard.id = resp.data;
                console.log($scope.newBoard);
                $scope.currentBoard = $scope.newBoard;
                $scope.fillBoardWithNotes($scope.currentBoard);
                $scope.newBoard = null
                document.querySelector('#closeCreateBoardButton').click();
            })

    }

    $scope.deleteBoard = function (id) {
        console.log('board id: ' + id)
        $http.delete(contextPath + '/board/' + id)
            .then(function (resp) {
                $scope.getBoards();
            })

    };

    //find all roles of the board
    $scope.getRoles = function () {
        console.log('get roles function')
        // GET http://localhost/api/v1/board-roles/{{boardId}}
        $http.get(contextPath + '/board-roles/' + $scope.currentBoard.id)
            .then(function (resp) {
                $scope.Roles = resp.data
                console.log($scope.Roles)
                $scope.linkRoleToUser($scope.user?.id)
                $scope.getBoardRoleTypes()
            })
    }
    //link the role to the user
    $scope.linkRoleToUser = function (userId) {
        $scope.userRole = $scope.Roles.find(r => r.userId === userId).role;
        console.log($scope.userRole)
    }

    //types of board roles
    $scope.getBoardRoleTypes = function () {
        $http.get(contextPath + '/board-roles/types')
            .then(function (resp) {
                $scope.RoleTypes = resp.data;
                console.log($scope.RoleTypes)
            });
    }

    $scope.setColor = function (objectC, color) {
        console.log(objectC)
        console.log(color)
        objectC.color = color
    }

    $scope.isAllowed = function (mode) {
        switch (mode) {
            case 0: {
                return $scope.userRole !== 'READER'
            }
            case 1: {
                return $scope.userRole === 'OWNER'
            }
        }
    }

    $scope.priorityUp = function (note) {
        console.log(note)
        if (!note.priority) note.priority = 0
        note.priority++
        if (note.priority > 9)
            note.priority = 0
    }
    $scope.priorityDown = function (note) {
        note.priority--
        if (note.priority < 0)
            note.priority = 9
    }
    $scope.isPriorityValid = function (text, note) {
        let priority = note.priority
        if (priority > -1 && priority < 10)
            return priority === parseInt(text)
        else return false
    }

    $scope.sortNotes = function (propertyName) {
        $scope.reverse = ($scope.propertyName === propertyName) ? !$scope.reverse : false;
        $scope.propertyName = propertyName;
    }
    $scope.colorComparator = function (v1, v2) {
        if (v1.type === 'string' || v2.type === 'string') {
            return ($scope.Colors.findIndex(c => c.colorHex === 'v1') < $scope.Colors.findIndex(c => c.colorHex === 'v2')) ? -1 : 1;
        }
        return v1.value.localeCompare(v2.value);
    }
    $scope.addChecklist = function (id) {
        let checkListHtml = '<div><input class="input-header py-2" ng-readonly="!isAllowed(0)" type="text" placeholder="checklist name..."/>' +
            '<span>{{n.header}}</span><input class="input-header py-2" ng-readonly="!isAllowed(0)" type="text" placeholder="checklist item..."/>' +
            '</div>'
        let temp = $compile(checkListHtml)($scope)
        angular.element(document.getElementById(id)).append(temp)
    }

    // Chat functions
    var stompClient = null;
    $scope.newMessage = null;

    connect = function () {
        // var endpoint = '/ws/?access_token=' + auth.access_token;
        var socket = new SockJS("/team-notes/secured/ws/");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    };

    const onConnected = () => {
        console.log("connected");
        stompClient.subscribe(
            "/secured/topic/" + $scope.currentBoard.id + "/secured/topic",
            onMessageReceived);
        // connectingElement.classList.add('hidden');
    };

    function onError(error) {
        // connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        // connectingElement.style.color = 'red';
    }


    disconnect = function () {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        // setConnected(false);
        console.log("Disconnected");
    }


    $scope.sendNewMessage = function(newMessage) {
        $scope.newMessage = newMessage;
        if (newMessage.trim() !== "" && stompClient) {
            const message = {
                message: $scope.newMessage,
                currenBoardId: $scope.currentBoard.id
            };
            stompClient.send("/app/secured/chat", {}, JSON.stringify(message));
            console.log("Your message " + newMessage + " was sent onto server");
            $scope.newMessage = null;

        }
        $scope.newMessage = null;
    };
    $scope.messageForm = document.querySelector('#messageForm');

    function onMessageReceived(payload) {
        var message = JSON.parse(payload.body);
        var messageElement = document.createElement('li');
        messageElement.classList.add('chat-message');

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }


    $scope.showChat = function () {
        console.log("'Show chat'-command from a boardId" + $scope.currentBoard.id)
        $http({
            url: "/team-notes/messages",
            method: "GET",
            params: {
                chatId: $scope.currentBoard.id
            }
        }).then(function successCallBack(response) {

            $scope.chatMessagesPage = response.data;
        }, function errorCallback(response) {
            console.log(response.data);
        });
    };

});