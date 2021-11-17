angular.module('app', []).controller('promoController', function ($scope, $http) {
    const contextPath = 'http://localhost:8180/team-notes/api/v1';

    $scope.newUser = null;

    $scope.signup = function (signupForm) {
        console.log('signup function started')
        if (signupForm.$valid) {
            $http.post(contextPath + '/signup', $scope.user)
                .then(function (resp) {
                    if (resp.data) {
                        $scope.user = resp.data.user
                        $scope.signupNotification = 'registration is successful'
                        document.querySelector('#closeUserButton').click()

                        location.replace('index.html')
                    } else $scope.signupNotification = 'User with this email already exists. Please signing.'
                })
        }
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
});