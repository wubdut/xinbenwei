
define(['authService'], function(authService) {

    var app = angular.module('registerModule', []);

    app.controller('registerCtl', function($scope, $http) {

        $scope.mytemplate = "template/register_action.html";
        $scope.formData = {};

        $scope.registerSubmit = function () {

            var data = {
                username : $scope.formData.username,
                mobile : $scope.formData.mobile,
                password : $scope.formData.password
            };

            if ($scope.formData.password === $scope.formData.firmpassword) {
                $http.post("http://" + authService.getURL() + ":8000/auth/register", data).then(
                    function(response) {
                        $scope.mytemplate = "template/register_success.html";
                        $scope.error = false;
                    },
                    function() {
                        $scope.error = true;
                        alert("注册失败，该邮箱已经注册");
                    }
                );
            } else {
                alert("两次输入的密码不同");
            }

        }

    });

});