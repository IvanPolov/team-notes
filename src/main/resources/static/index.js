angular.module('app', []).controller('indexController', function ($scope, $http) {
    const contextPath = 'http://localhost:8180/team-notes/api/v1';


    $scope.acronym = function (sentence, size) {
        console.log('acronym: ' + sentence + 'size: ' + size);
        let acro = sentence.split(' ').map(x => x[0]).join('').slice(0, size).toUpperCase();
        console.log(acro);
        return acro;
    }

    $scope.saveNote = function () {
        $scope.newNote.board = $scope.currentBoard;
        console.log($scope.newNote)
        $http.post(contextPath + '/note', $scope.newNote)
            .then(function (resp) {
                $scope.fillBoardWithNotes($scope.newNote.board);
                $scope.newNote = null
            })

    }

    $scope.updateNote = function () {
        console.log($scope.currentBoard)
        $http.put(contextPath + '/note', this.n)
            .then(function (resp) {
                $scope.fillBoardWithNotes($scope.currentBoard);
            })

    }
    $scope.deleteNote = function (id) {
        console.log('product id: ' + id)
        $http.delete(contextPath + '/note/' + id)
            .then(function (resp) {
                $scope.fillBoardWithNotes();
            })

    };

    $scope.fillBoardWithNotes = function (currentBoard) {
        console.log('board id: ' + currentBoard.id)
        $scope.currentBoard = currentBoard;
        $http({
            url: contextPath + '/board/' + currentBoard.id + '/notes',
            method: 'GET'
        }).then(function (response) {
            $scope.Notes = response.data;
        });
    };

    $scope.getBoards = function () {
        console.log('get all boards function');
        $http({
            url: contextPath + '/board',
            method: 'GET'
        }).then(function (response) {
            $scope.Boards = response.data;
            console.log($scope.Boards);
        });
    }

    $scope.updateBoard = this.board;

    $scope.updateBoard = function () {
        $http.put(contextPath + '/board', this.board)
            .then(function (resp) {
                $scope.getBoards();
            })

    }

    $scope.saveBoard = function () {
        console.log($scope.newBoard)
        $http.post(contextPath + '/board', $scope.newBoard)
            .then(function (resp) {
                $scope.newBoard = null
                $scope.getBoards();
            })

    }

    $scope.deleteBoard = function (id) {
        console.log('board id: ' + id)
        $http.delete(contextPath + '/board/' + id)
            .then(function (resp) {
                $scope.getBoards();
            })

    };

    $scope.getBoards();

    $scope.auto_grow =  function (element) {
        element.style.height = "5px";
        element.style.height = (element.scrollHeight)+"px";
    }


});