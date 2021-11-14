angular.module('app', []).controller('promoController', function ($scope, $http) {
    const contextPath = 'http://localhost:8180/team-notes/api/v1';

    $scope.newUser = null;

    $scope.signup = function (){
        console.log('signup function started')
        $http.post(contextPath + '/signup',$scope.newUser)
            .then(function (resp){
                if(resp.data) {
                    $scope.signupNotification = 'registration is successful'
                    document.querySelector('#closeUserButton').click()
                }else $scope.signupNotification = 'email already exists'
            })
    };

    $scope.validatePassword = function (){
        if($scope.newUser.password !== $scope.newUser.confirmPassword){
            $scope.signupNotification = "Passwords don't match"
            return false;
        }
        else {
            $scope.signupNotification = '';
            return true;
        }
    };
    // $scope.validateEmail = function (){
    //     $http.get(contextPath + '/signup',$scope.newUser.email)
    //         .then(function (resp){
    //                 $scope.signupNotification = resp.data;
    //         })
    // };
});