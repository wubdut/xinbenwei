
define([''], function() {

    var app = angular.module('accountModule', []);

    app.controller('accountCtl', function($scope, $http) {

        $scope.mytemplate = "template/recommend_today.html";

        $scope.recommendToday = function() {
            $scope.mytemplate = "template/recommend_today.html";
        };

        $scope.recommendWeek = function () {
            $scope.mytemplate = "template/recommend.html";
        };

        $scope.recommendMonth = function () {
            $scope.mytemplate = "template/recommend.html";
        };

        $scope.settings = function () {
            $scope.mytemplate = "template/settings.html";
        };

        $scope.userProfile = function () {
            $scope.mytemplate = "template/user_profile.html";
        };

        $scope.afterLoad = function () {
            if ($scope.mytemplate === "template/recommend.html") {
                $('#dataTables-example').DataTable({
                    responsive: true
                });
            }
        };

    });

});