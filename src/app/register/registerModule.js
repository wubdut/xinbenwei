
define(['jquery'], function(jquery) {

    var app = angular.module('registerModule', []);

    app.controller('registerCtl', function($scope, $http) {

        $scope.mytemplate = "template/register_action.html";
        $scope.formData = {};

        $scope.registerSubmit = function () {

            var email = $scope.formData.email;
            var password = $scope.formData.password;
            var firmpassword = $scope.formData.firmpassword;

            var data = {
                username : $scope.formData.email,
                password : $scope.formData.password
            };

            if (password === firmpassword) {
                $http.post('http://localhost:8080/auth/register', data).then(
                    function(response) {
                        $scope.mytemplate = "template/register_success.html";
                        $scope.error = false;
                    },
                    function() {
                        $scope.error = true;
                        alert("注册失败");
                    }
                );
            } else {
                alert("两次输入的密码不同");
            }

        }

    });

});