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

    $scope.signup = function (signupForm) {
        console.log('signup function started')
        if (signupForm.$valid) {
            $http.post(contextPath + '/signup', $scope.user, httpOptions)
                .then(function success(resp) {
                        $scope.message = 'User added!';
                        $scope.errorMessage = '';
                        $scope.user = null;
                        $scope.submitted = false;
                        // $scope.user = resp.data.user
                        // $scope.signupNotification = 'registration is successful'
                        document.querySelector('#closeUserButton').click()
                        // location.replace('index.html')
                },
                function error(resp){
                    if(resp.status === 409){
                        $scope.errorMessage = resp.data.message;
                    }
                    else {
                        $scope.errorMessage = 'Error adding user!';
                    }
                    $scope.message = '';
                });
        }
    };

    $scope.validateEmail = function (signupForm){
        console.log('validate email')
        if (signupForm.email.$valid) {
            $http.get(contextPath + '/signup/', {params: {email:$scope.user.email, httpOptions}})
                .then(function success(resp) {
                            $scope.message = 'Seems good!';
                            $scope.errorMessage = '';
                            $scope.isEmailValid = true;
                            console.log('is email valid?')
                            console.log($scope.isEmailValid)
                    },
                    function error(resp){
                        $scope.isEmailValid = false;
                        console.log('error: is email valid?')
                        console.log($scope.isEmailValid)
                        if(resp.status === 409){
                            $scope.errorMessage = resp.data[0];
                            console.log ($scope.errorMessage)
                        }
                        else {
                            $scope.errorMessage = 'Error adding user!';
                        }
                        $scope.message = '';
                    });
        }
    }

    $scope.validatePassword = function (){
        if($scope.user.password !== $scope.user.confirmPassword){
            $scope.signupNotification = "Passwords don't match"
            $scope.isPasswordValid = false;
            return false;
        }
        else {
            $scope.isPasswordValid = true;
            $scope.signupNotification = '';
            return true;
        }
    };
});