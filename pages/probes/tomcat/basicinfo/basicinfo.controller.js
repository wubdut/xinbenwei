(function() {
    'use strict';
    /**
     * wub-neu
     * 2018.07.23
     * @group Controller
     * @name LoginCtrl
     * @class
     */
    angular.module('inspinia')
        .controller( 'BasicinfoCtrl', ['$scope', '$http', '$state', '$window', 'AuthService',
            function ($scope, $http, $state, $window, authService) {

                $scope.getData = function () {
                    $http.get(
                        authService.getURL() + "/basicinfo/rsapm28",
                        {headers : authService.createAuthorizationTokenHeader()}
                    ).then(function (response) {
                        console.log("yes");
                        console.log(response.data);
                        var data = response.data;

                        $scope.applicationName = data.applicationName;
                        $scope.agentVersion = data.agentVersion;
                        $scope.agentType = data.agentId;
                        $scope.pid = data.pid;
                        $scope.hostName = data.hostName;
                        $scope.jvmInfo = data.jvmInfo;
                        $scope.ip = data.ip;
                        $scope.startTimestamp = data.startTimestamp;
                        $scope.serverMetaData = data.serverMetaData;
                        $scope.status = data.status;
                        $scope.currentServiceInfo = [];
                        $scope.currentPeriod = '30m';
                        for (var i = 0; i < data.serverMetaData.serviceInfos.length; i++) {
                            if (data.serverMetaData.serviceInfos[i].serviceLibs.length > 0) {
                                $scope.currentServiceInfo = data.serverMetaData.serviceInfos[i];
                                break;
                            }
                        }
                    }, function () {
                        console.log("basicinfo no data");
                    });

                };

                $scope.selectServiceInfo = function(serviceInfo) {
                    // alert(serviceInfo);
                    if (serviceInfo.serviceLibs.length > 0) {
                        $scope.currentServiceInfo = serviceInfo;
                    }
                };

                $scope.timeperiod = function ( period ) {
                    $scope.currentPeriod = period;
                };

                $scope.getData();

            }
        ]);
})();