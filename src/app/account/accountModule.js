
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
        $scope.afterLoad = todayLoad();

        /**************************** 基本操作 ****************************/
        $scope.logout = function () {
            authService.removeJwtToken();
        };


        /****************************** 加载不同的模板 ****************************/
        $scope.recommendToday = function() {
            $scope.mytemplate = "template/recommend_today.html";
            $scope.afterLoad = todayLoad();
        };

        $scope.recommendWeek = function () {
            $scope.mytemplate = "template/recommend.html";
            $scope.afterLoad = weekLoad();
        };

        $scope.recommendMonth = function () {
            $scope.mytemplate = "template/recommend.html";
            $scope.afterLoad = monthLoad();
        };

        $scope.shortSwingService = function () {
            $scope.mytemplate = "template/shortswing_service.html";
            $scope.afterLoad = function () {};
        };

        $scope.diagnoseService = function () {
            $scope.mytemplate = "template/diagnose_service.html";
            $scope.afterLoad = function () {};
        };

        $scope. assistService = function () {
            $scope.mytemplate = "template/assist_service.html";
            $scope.afterLoad = function () {};
        };

        $scope.userProfile = function () {
            $scope.mytemplate = "template/user_profile.html";
            $scope.afterLoad = userProfileLoad();
        };

        $scope.myOrders = function () {
            $scope.mytemplate = "template/my_orders.html";
            $scope.afterLoad = function () {};
        };

        $scope.settings = function () {
            $scope.mytemplate = "template/settings.html";
            $scope.afterLoad = function () {};
        };


        /************************ 模板加载完成之后的操作 ************************/

        $scope.$on('$includeContentLoaded', function (event, target) {
            // it has loaded!
            switch(target) {
                case 'template/recommend.html':
                    datatablesLoad();
                break;
            }

        });

        function datatablesLoad() {
            // console.log('datatableload', $('#dataTablesExample'));
            $(dataTablesExample).DataTable({
                responsive: true,
                bRetrieve: true
            });
        }

        function todayLoad() {
            $http.get(
                "http://" + authService.getURL() + ":8000/api/shortswing",
                {headers : authService.createAuthorizationTokenHeader()}
            ).then(function (response) {
                $scope.shortSwingToday = [];
                for (var i = 0; i < response.data.length; i++) {
                    item = response.data[i];
                    if (!item.sale) {
                        $scope.shortSwingToday.push(item);
                    }
                }
            });
        }

        function weekLoad() {
            $http.get(
                "http://" + authService.getURL() + ":8000/api/shortswing",
                {headers : authService.createAuthorizationTokenHeader()}
            ).then(function (response) {
                $scope.shortSwing = [];
                for (var i = 0; i < response.data.length; i++) {
                    item = response.data[i];
                    if (item.sale) {
                        $scope.shortSwing.push(item);
                    }
                }
            }, function () {
                window.location.href = "../login/login.html";
            });
        }

        function monthLoad() {
            $http.get(
                "http://" + authService.getURL() + ":8000/api/shortswing",
                {headers : authService.createAuthorizationTokenHeader()}
            ).then(function (response) {
                $scope.shortSwing = [];
                for (var i = 0; i < response.data.length; i++) {
                    item = response.data[i];
                    if (item.sale) {
                        $scope.shortSwing.push(item);
                    }
                }
            }, function () {
                window.location.href = "../login/login.html";
            });
        }
        
        function userProfileLoad() {
            $http.get(
                "http://" + authService.getURL() + ":8000/api/whoami",
                {headers : authService.createAuthorizationTokenHeader()}
            ).then(function (response) {
                $scope.userInfo = response.data;
            }, function () {
                window.location.href = "../login/login.html";
            });
        }

    });


    /************************ 过滤器 ************************/
    app.filter('PercentValue', function () {
        return function (o) {
            if (o != undefined && /(^(-)*\d+\.\d*$)|(^(-)*\d+$)/.test(o)) {
                var v = parseFloat(o);
                return Number(Math.round(v * 10000) / 100).toFixed(2) + "%";
            } else {
                return "undefined";
            }
        }
    });

});