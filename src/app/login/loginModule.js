
define(['authService'], function(authService) {

    var app = angular.module('loginModule', []);

    app.controller('loginCtl', function($scope, $http) {

        $scope.loginSubmit = function (formData) {
            var data = {
                username : formData.username,
                password : formData.password
            };
            // alert(data.email);
            $http.post("http://" + authService.getURL() + ":8000/auth/login", data).then(
                function(response) {
                    authService.setJwtToken(response.data.access_token);
                    // alert(authService.getJwtToken());
                    $scope.error = false;
                    window.location.href = "../account/account.html"
                },
                function() {
                    $scope.error = true;
                    alert("密码或用户名错误");
                }
            );
        }

    });

});