angular.module('app', []).controller('indexController', function ($rootScope, $scope, $http, $interval, $compile) {
    const contextPath = 'http://localhost:8180/team-notes/api/v1';

    $scope.invitedUser = null;
    $scope.foundUser = null;
    $scope.isFounded = false;
    $scope.Users = [];

    $scope.isChatConnected = false;
    $scope.currentChatBoardId = null;
    $scope.isShowChat = false;
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
        // $scope.disconnect();
        console.log('add user func')
        // PUT http://localhost:8180/api/v1/board/{{boardId}}/addUser
        $http.get(contextPath + '/board/' + $scope.currentBoard.id + '/addUser/' + $scope.foundUser.id)
            .then(function () {
                console.log('add user func resp')
                $scope.invitedUser.email = null;
                console.log($scope.foundUser)
                $scope.Users.push($scope.foundUser);
                $scope.isFounded = false;
                connect();

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
        $scope.disconnect();

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
        $scope.disconnect();
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
        $scope.disconnect();
        //$scope.isShowChat=false;
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
                $scope.disconnect();
                $scope.isShowChat=false;
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
    let stompClient = null;
    $scope.newMessage = null;

    let connect = function () {
        if (!$scope.isChatConnected) {
            const socket = new SockJS("/team-notes/secured/ws/");
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected, onError);
            $scope.isChatConnected = true;
            // $scope.isShowChat = true;
        }
    };

    const onConnected = () => {
        console.log("connected");
        stompClient.subscribe("/secured/topic/" + $scope.currentBoard.id,  //this set destination for subscribing for recive of all new message for this board
            onMessageReceived);  // this is the consumer of all messages received for this board
    };

    function onError(error) {
        $scope.disconnect();
        // connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        // connectingElement.style.color = 'red';
    }

    $scope.disconnect = function () {
        if (stompClient !== null) {
            stompClient.disconnect();

        }
        $scope.isShowChat=false;
        $scope.isChatConnected = false;
        // setConnected(false);
        console.log("Disconnected");
    }

    $scope.sendNewMessage = function (newMessage) {
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

    onMessageReceived = function (payload) {
        var message = JSON.parse(payload.body);
        console.log("Answer message recived");
        console.log("Answer message body " + payload.body);

        var messageElement = document.createElement('li');
        messageElement.classList.add('chat-message');

        var usernameElement = document.createElement('b');
        var usernameText = document.createTextNode(message.senderName);
        var dateElement = document.createElement('small');
        var iElement = document.createElement('i');
        var brElement = document.createElement('br');
        var dateTimeText = document.createTextNode(new Date(message.sentMessageDate).toLocaleString("en-US"));
        usernameElement.appendChild(usernameText);
        iElement.appendChild(dateElement);
        dateElement.appendChild(dateTimeText);
        messageElement.appendChild(usernameElement);
        messageElement.appendChild(brElement);
        messageElement.appendChild(iElement);

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.message);
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
    $scope.disconnect();
})
;