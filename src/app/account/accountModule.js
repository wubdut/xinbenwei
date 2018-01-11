
define(['authService', 'jquery'], function(authService,jquery) {

    var app = angular.module('accountModule', []);

    app.controller('accountCtl', function($scope, $http) {

        /**************************** 默认显示 ****************************/
        $http.get(
            "http://" + authService.getURL() + ":8000/api/whoami",
            {headers : authService.createAuthorizationTokenHeader()}
            ).then(function (response) {
            $scope.username = response.data.username;
        });
        $scope.mytemplate = "template/recommend_today.html";
        $scope.afterLoad = defaultLoad;

        /**************************** 基本操作 ****************************/
        $scope.logout = function () {
            authService.removeJwtToken();
        };


        /****************************** 加载不同的模板 ****************************/
        $scope.recommendToday = function() {
            $scope.mytemplate = "template/recommend_today.html";
        };

        $scope.recommendWeek = function () {
            $scope.mytemplate = "template/recommend.html";
            $scope.afterLoad = defaultLoad;
        };

        $scope.recommendMonth = function () {
            $scope.mytemplate = "template/recommend.html";
            $scope.afterLoad = defaultLoad;
        };

        $scope.settings = function () {
            $scope.mytemplate = "template/settings.html";
        };

        $scope.userProfile = function () {
            $scope.mytemplate = "template/user_profile.html";
        };


        //************************ 模板加载完成之后的操作 ************************/
        var defaultLoad = function () {
            $('#dataTables-example').DataTable({
                responsive: true
            });
        };


    });
});