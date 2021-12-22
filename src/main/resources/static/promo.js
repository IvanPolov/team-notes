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
    $scope.isErrorLogin = false;
    $scope.signupSuccessModal = new bootstrap.Modal(document.getElementById('signupSuccessModal'), {
        keyboard: false
    })
    $scope.loginModal = new bootstrap.Modal(document.getElementById('login'), {
        keyboard: false
    })
    $scope.signupForm = document.getElementById('signupForm')
    $scope.ringRegistrationModal = new bootstrap.Modal(document.getElementById('ringRegistrationModal'), {
        keyboard: false
    })
    $scope.errorModal = new bootstrap.Modal(document.getElementById('errorModal'), {
        keyboard: false
    })

    $scope.signup = function (signupForm) {
        document.querySelector('#closeUserButton').click()
        $scope.ringRegistrationModal.show()
        console.log('signup function started')
        if (signupForm.$valid) {
            $http.post(contextPath + '/signup', $scope.user, httpOptions)
                .then(function success(resp) {
                        $scope.message = resp.data.detail
                        $scope.isEmailSended = true
                        $scope.errorMessage = ''
                        $scope.user = null
                        $scope.submitted = false
                        $scope.ringRegistrationModal.hide()
                        $scope.signupSuccessModal.show()
                    },
                    function error(resp) {
                        $scope.ringRegistrationModal.hide()
                        $scope.errorModal.show()
                        $scope.user.email = '';
                        $scope.errorTitle = resp.title
                        $scope.errorMessage = resp.detail
                        $scope.message = 'We can\'t send message to your email. Perhaps the specified email does not exist'
                    });

        }

    };

    $scope.enterIntoService = function () {
        $scope.signupSuccessModal.dispose();

        location.replace('index.html');

    };

    $scope.login = function (email, password) {
        var data = {
            email: email,
            password: password,
        };
        console.log('login function started')

        $http.post(contextPath + '/login', data)
            .then(function success(resp) {
                    $scope.errorMessage = '';
                    $scope.submitted = false;
                    $scope.loginModal.dispose();
                    location.replace('index.html');
                },
                function error(resp) {
                    $scope.isErrorLogin = true;
                    if (resp.status === 401) {
                        $scope.errorMessage = resp.data.detail;
                    } else {
                        $scope.errorMessage = "You entered incorrect data";
                    }
                    console.log($scope.errorMessage)
                    $scope.message = '';
                });

    };

    $scope.validateEmail = function (signupForm) {
        console.log('validate email')
        if (signupForm.email.$valid) {
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
                        console.log(resp.data)
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