angular.module('app', []).controller('promoController', function ($scope, $http) {
    const contextPath = 'http://localhost:8180/team-notes/api/v1';

    $scope.newUser = null;
    $scope.errNotif = false;
    $scope.signup = function (signupForm) {
        console.log('signup function started')
        if (signupForm.$valid) {
            $http.post(contextPath + '/signup', $scope.user)

                .then(function successCallback(response) {
                    if (response.data != null) {
                        $scope.user = response.data.user
                        // $scope.signupNotification = 'registration is successful'
                        document.querySelector('#closeUserButton').click()
                        location.replace('index.html')
                    }
                }, function errorCallback(response) {
                    // if (response.data.errors > 0) {
                        $scope.listErrors = response.data.errors
                    // }
                });
        }
    };

    $scope.validatePassword = function () {
        if ($scope.user.password !== $scope.user.confirmPassword) {
            $scope.errNotif = true;
            $scope.signupNotification = "Passwords don't match! "
            return false;
        } else {
            $scope.errNotif = true;
            $scope.signupNotification = 'OK! Passwords matched';
            return true;
        }
    };


});