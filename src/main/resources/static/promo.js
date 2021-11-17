angular.module('app', []).controller('promoController', function ($rootScope, $scope, $http) {
    const contextPath = 'http://localhost:8180/team-notes/api/v1';

    $scope.newUser = null;

    $scope.signup = function (){
        console.log('signup function started')
        $http.post(contextPath + '/signup',$scope.user)
            .then(function (resp){
                if(resp.data) {
                    $scope.user = resp.data.user
                    $scope.signupNotification = 'registration is successful'
                    document.querySelector('#closeUserButton').click()
                    location.replace('index.html')
                }else $scope.signupNotification = 'email already exists'
            })
    };

    $scope.validatePassword = function (){
        if($scope.user.password !== $scope.user.confirmPassword){
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