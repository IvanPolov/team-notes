angular.module('app', []).controller('indexController', function ($rootScope, $scope, $http, $interval, $compile) {
    $scope.messageArea = undefined;
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
    $scope.newMessage = "";
    $scope.command = null;
    $scope.sendingMmessage = null;
    $scope.scrollStop = false;
    const ChatCommands = {
        UPDATE_Board: "UPDATE_Board",
        UPDATE_Note: "UPDATE_Note",
        DELETE_Note: "DELETE_Note",
        CHAT_Message: "CHAT_Message"
    }
    $scope.ChatMessageArray = [];

    $scope.ChatMessageArea = document.getElementById("messageArea");
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
                document.querySelector('#closeNoteButton').click();
            });

        $scope.sendingMmessage = {
            command: ChatCommands.UPDATE_Board,
            message: "Created new Note...",
            currenBoardId: $scope.currentBoard.id
        }
        sendMessage();
        $scope.sendingMmessage = null;
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
            });
        $scope.sendingMmessage = {
            command: ChatCommands.UPDATE_Note,
            message: "Updated Note...",
            currenBoardId: $scope.currentBoard.id
        }
        sendMessage();
        $scope.sendingMmessage = null;
    }
    $scope.deleteNote = function (id) {
        console.log('note id: ' + id)
        $http.delete(contextPath + '/note/' + id)
            .then(function (resp) {
                $scope.fillBoardWithNotes($scope.currentBoard);
            });

        $scope.sendingMmessage = {
            command: ChatCommands.DELETE_Note,
            message: "Deleted Note...",
            currenBoardId: $scope.currentBoard.id
        }
        sendMessage();
    };

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
            });
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
                $scope.isShowChat = false;
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
        $scope.sendingMmessage = {
            command: ChatCommands.UPDATE_Board,
            message: "Sorted Notes...",
            currenBoardId: $scope.currentBoard.id
        }
        sendMessage();
        $scope.sendingMmessage = null;
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

    // <<<<<<<<<<<<<<<< Chat functions >>>>>>>>>>>>>>>>>>
    let stompClient = null;

    let connect = function () {
        if (!$scope.isChatConnected) {
            const socket = new SockJS("/team-notes/secured/ws/");
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected, onError);
            $scope.isChatConnected = true;
        }
    };

    const onConnected = () => {
        console.log("connected");
        stompClient.subscribe("/secured/topic/" + $scope.currentBoard.id,  //this set destination for subscribing for recive of all new message for this board
            onMessageReceived);  // this is the consumer of all messages received for this board

        $scope.sendingMmessage = {
            command: ChatCommands.CHAT_Message,
            message: "User with name '" + $scope.user.username + "' entered the chat.",
            currenBoardId: $scope.currentBoard.id
        }
        sendMessage();
        $scope.sendingMmessage = null;
        $scope.getLastMessagesFromChatHistoryDB();
    };


    function onError(error) {
        $scope.disconnect();
    }

    $scope.disconnect = function () {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        $scope.isShowChat = false;
        $scope.isChatConnected = false;
        console.log("Disconnected");
    }

    $scope.sendNewMessage = function () {
        $scope.sendingMmessage = {
            command: $scope.CHAT_Message,
            message: $scope.newMessage,
            currenBoardId: $scope.currentBoard.id
        }
        sendMessage();
        $scope.newMessage = "";
        $scope.sendingMmessage = null;
    }
    sendMessage = function () {
        if (stompClient) {
            stompClient.send("/app/secured/chat", {}, JSON.stringify($scope.sendingMmessage));
        }
    };

    onMessageReceived = function (payload) {
        let message = JSON.parse(payload.body);
        console.log("Answer message body " + payload.body);
        switch (message.command) {
            case 'UPDATE_Board':
                $scope.fillBoardWithNotes($scope.currentBoard);
                break;
            case 'UPDATE_Note':
                $scope.fillBoardWithNotes($scope.currentBoard);
                break;
            case 'DELETE_Note':
                $scope.fillBoardWithNotes($scope.currentBoard);
                break;
        }
        $scope.getLastMessageFromChatHistoryDB();
    };

    $scope.getLastMessageFromChatHistoryDB = function () {
        console.log("Get last message from chat history DB")
        $http({
            url: "/team-notes/messages/get-one/",
            method: "GET",
            params: {
                chatId: $scope.currentBoard.id
            }
        }).then(function successCallBack(response) {
            $scope.scrollStop = false;
            let minPageIndex = 0;
            response.data.sentMessageDate = new Date(response.data.sentMessageDate).toLocaleString("en-US");
            $scope.ChatMessageArray.push(response.data)
            console.log(response.data);
        }, function errorCallback(response) {
            console.log(response.data);
        });
    };

    $scope.getLastMessagesFromChatHistoryDB = function () {
        console.log("Get last 10 messages (one page) from chat history DB")
        $http({
            url: "/team-notes/messages",
            method: "GET",
            params: {
                chatId: $scope.currentBoard.id
            }
        }).then(function successCallBack(response) {
            $scope.scrollStop = false;
            if (response.data !== null) {
                let temParr = [];
                temParr = response.data.content;
                temParr.forEach(function (item, i, temParr) {
                    item.sentMessageDate = new Date(item.sentMessageDate).toLocaleString("en-US");
                });
                $scope.ChatMessageArray = temParr;
            }
        }, function errorCallback(response) {
            console.log(response.data);
        });
        $scope.scrollNext();
        $scope.messageAreaScrollTop();
    };

    $scope.messageAreaScrollTop = function () {
        if (!$scope.scrollStop) {
            messageArea.scrollTop = messageArea.scrollHeight;
        }
    };

    $scope.toggleChatWindow = function (id) {
        if (document.getElementById(id).style.display === 'none') {
            document.getElementById(id).style.display = 'block';
            messageArea.scrollTop = messageArea.scrollHeight;
        } else {
            document.getElementById(id).style.display = 'none';
        }
    };

    $scope.checkScrolling = function () {
        $scope.checkScrolling.scrollCheck = messageArea.scrollTop === 0;
        if ($scope.checkScrolling.scrollCheck) {
            if ($scope.ChatMessageArray[0].id !== 1) {
                messageArea.scrollTop = 3;
                $scope.getPreviousMessagesFromChatHistoryDB();
            }
        }
    }

    $scope.scrollNext = function () {
        $scope.ChatMessageArea.onscroll = $scope.checkScrolling;
        $scope.checkScrolling.call($scope.ChatMessageArea);
    };

    $scope.getPreviousMessagesFromChatHistoryDB = function () {

        console.log("Get last previous 10 messages from chat history DB")
        $http({
            url: "/team-notes/history",
            method: "GET",
            params: {
                chatId: $scope.currentBoard.id,
                firstID: $scope.ChatMessageArray[0].id - 1
            }
        }).then(function successCallBack(response) {
            $scope.scrollStop = true;
            if (response.data !== null) {
                let temParr = [];
                temParr = response.data;
                temParr.forEach(function (item, i, temParr) {
                    item.sentMessageDate = new Date(item.sentMessageDate).toLocaleString("en-US");
                });
                $scope.ChatMessageArray = temParr.concat($scope.ChatMessageArray);
                console.log($scope.ChatMessageArray[0].id)
            }
        }, function errorCallback(response) {
            console.log(response.data);
        });
    };

    $scope.tryToLogout = function () {
        $scope.disconnect();
        $scope.clearUser();
    };

    $scope.clearUser = function () {
        $scope.user = null;
        $http.defaults.headers.common.Authorization = '';
        $scope.invitedUser = null;
        $scope.foundUser = null;
        $scope.isFounded = false;
        $scope.Users = null;

        $scope.isChatConnected = false;
        $scope.currentChatBoardId = null;
        $scope.isShowChat = false;
        $scope.newMessage = "";
        $scope.command = null;
        $scope.sendingMmessage = null;
        $scope.ChatMessageArray = null;
        location.replace('promo.html');
    };

})
;