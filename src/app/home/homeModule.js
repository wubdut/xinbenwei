
define(['authService'], function(authService) {

    var app = angular.module('homeModule', []);

    app.controller('homeCtl', function($scope, $http) {

        $http.get(
            "http://" + authService.getURL() + ":8000/api/whoami",
            {headers : authService.createAuthorizationTokenHeader()}
        ).then(function (response) {
            $scope.mytemplate = 'template/loged.html';
            $scope.username = response.data.username;
        }, function () {
            $scope.mytemplate = 'template/unloged.html';
        });

    });

});