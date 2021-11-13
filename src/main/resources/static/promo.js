angular.module('app', []).controller('promoController', function ($scope, $http) {
    const contextPath = 'http://localhost:8180/team-notes/api/v1';

    $scope.newUser = null;

    $scope.signup = function (){
        $http.post(contextPath + '/signup',$scope.newUser)
    };

    $scope.validatePassword = function (){
        if($scope.newUser.password !== $scope.newUser.confirmPassword){
            $scope.signupAnswer = "Passwords don't match"
            return false;
        }
        else {
            $scope.signupAnswer = '';
            return true;
        }
    };
});