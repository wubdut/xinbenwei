(function() {
    'use strict';
    /**
     * wub-neu
     * 2018.07.26
     * @group Controller
     * @name RsCtrl
     * @class
     */
    angular.module('inspinia')
        .controller( "RsCtrl", ["$scope", '$http', "$state", '$window', '$location', 'AuthService',
            function ($scope, $http, $state, $window, $location, authService) {
                if ($location.url() !== "/landing" && $location.url() !== "/login") {
                    $http.get(
                        authService.getURL() + "/api/whoami",
                        {headers: authService.createAuthorizationTokenHeader()}
                    ).then(function (response) {
                        // alert(authService.getJwtToken());
                        // console.log(response.data);
                        $scope.username = response.data.username;
                        $scope.role = "User";
                        for (var i = 0; i < response.data.authorities.length; i++) {
                            if (response.data.authorities[i].authority === "ROLE_ADMIN") {
                                $scope.role = "Admin";
                            }
                        }
                        console.log("已登录");
                    }, function () {
                        console.log("未登录");
                        console.log($location.url());
                        $state.go('login');
                    });
                }

                $scope.logout = function () {
                    authService.removeJwtToken();
                    $state.go('login').then(
                        function() {
                            $window.location.reload();
                        }
                    );
                };
            }
        ]);
})();