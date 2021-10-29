angular.module('app', []).controller('indexController', function ($scope, $http) {
    const contextPath = 'http://localhost:8180/team-notes/api/v1';
    $scope.saveNote = function () {
        console.log($scope.newNote)
        $http.post(contextPath + '/note', $scope.newNote)
            .then(function (resp) {
                $scope.newNote = null
                $scope.fillTable();
            })

    }
    $scope.deleteNote = function (id) {
        console.log('product id: '+id)
        $http.delete(contextPath + '/note/'+ id)
            .then(function (resp){
                $scope.fillTable();
            })

    };

    $scope.fillTable = function () {
        $http({
            url: contextPath + '/note',
            method: 'GET'
        }).then(function (response) {
            $scope.Notes = response.data;
        });
    };

    $scope.fillTable();

    function auto_grow(element) {
        element.style.height = "5px";
        element.style.height = (element.scrollHeight)+"px";
    }
});