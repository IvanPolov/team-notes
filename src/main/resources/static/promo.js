angular.module('app', []).controller('promoController', function ($scope, $http) {
    const contextPath = 'http://localhost:8180/team-notes/api/v1';

    const httpOptions = {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    };
    $scope.newUser = null;
    $scope.isEmailValid = false;
    $scope.isPasswordValid = false;
    $scope.isEmailSended = false;

    $scope.signup = function (signupForm) {
        console.log('signup function started')
        if (signupForm.$valid) {
            $http.post(contextPath + '/signup', $scope.user, httpOptions)
                .then(function success(resp) {
                        $scope.message = resp.data.detail;
                        $scope.isEmailSended = true;
                        $scope.errorMessage = '';
                        $scope.user = null;
                        $scope.submitted = false;
                        // $scope.user = resp.data.user
                        // $scope.signupNotification = 'registration is successful'
                        document.querySelector('#closeUserButton').click()
                        setTimeout(
                            () => {
                                location.replace('index.html');
                            },
                            3 * 1000
                        );


                    },
                    function error(resp) {
                        $scope.errorTitle = resp.title;
                        $scope.errorMessage = resp.detail;

                        $scope.message = '';
                    });
        }
    };

    $scope.validateEmail = function (signupForm) {
        console.log('validate email')
        if (signupForm.email.$valid) { //
            $http.get(contextPath + '/signup/', {params: {email: $scope.user.email, httpOptions}})
                .then(function success(resp) {
                        $scope.message = 'resp.data.detail';

                        $scope.errorMessage = '';
                        $scope.isEmailValid = true;
                        console.log('is email valid?')
                        console.log($scope.isEmailValid)
                    },
                    function error(resp) {
                        $scope.isEmailValid = false;
                        console.log('error: is email valid?')
                        console.log($scope.isEmailValid)
                        if (resp.statusText === "BadRequest" || resp.statusText === "Conflict") {
                            $scope.errorMessage = resp.data.detail;
                        } else {
                            $scope.errorMessage = resp.data.detail;
                        }
                        $scope.message = '';
                    });
        }
    }

    $scope.validatePassword = function () {
        if ($scope.user.password !== $scope.user.confirmPassword) {
            $scope.signupNotification = "Passwords don't match"
            $scope.isPasswordValid = false;
            return false;
        } else {
            $scope.isPasswordValid = true;
            $scope.signupNotification = '';
            return true;
        }
    };
});