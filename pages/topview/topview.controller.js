(function() {
    'use strict';
    /**
     * wub-neu
     * 2018.07.18
     * @group Controller
     * @name TopviewCtrl
     * @class
     */
    angular.module('inspinia')
        .controller( "TopviewCtrl", ["$scope", '$http', "$state", '$window', 'AuthService',
            function ($scope, $http, $state, $window, authService) {
                var nodes = [];
                var links = [];
                $scope.model = new go.GraphLinksModel(nodes,links);
                $scope.model.selectedNodeData = null;
                $http.get(
                    authService.getURL() + "/topview",
                    {headers : authService.createAuthorizationTokenHeader()}
                ).then(function (response) {
                    console.log("yes");
                    nodes = response.data.nodes;
                    console.log(response.data.nodes);
                    links = response.data.links;
                    console.log(response.data.links);
                    $scope.model = new go.GraphLinksModel(nodes,links);
                    $scope.model.selectedNodeData = null;
                }, function () {
                    console.log("topview no data");
                    // $state.go('login');
                });


                // var nodes = [
                //     { key: "id_01", name: "MEMCACHED", type: "memcached", alerts: 0, info: "告警：0\n"},
                //     { key: "id_02", name: "JAVA", type: "java", alerts: 4, info: "告警：4\n内容：堆溢出、死锁、..."},
                //     { key: "id_03", name: "REDIS", type: "redis", alerts: 0, info: "告警：0\n"},
                //     { key: "id_04", name: "MYSQL", type: "mysql", alerts: 1, info: "告警：0\n"},
                //     { key: "id_05", name: "TOMCAT", type: "tomcat", alerts: 0, info: "告警：0\n"},
                //     { key: "id_06", name: "NGINX", type: "nginx", alerts: 0, info: "告警：0\n"},
                //     { key: "id_07", name: "ORACLE", type: "oracle", alerts: 0, info: "告警：0\n"}
                // ];
                //
                // var links = [
                //     { from: "id_01", to: "id_02", flow: "5" },
                //     { from: "id_02", to: "id_03", flow: "10" },
                //     { from: "id_02", to: "id_04", flow: "7" },
                //     { from: "id_05", to: "id_04", flow: "5" },
                //     { from: "id_05", to: "id_03", flow: "13" },
                //     { from: "id_05", to: "id_06", flow: "8" },
                //     { from: "id_04", to: "id_07", flow: "6" }
                // ];

                $scope.model = new go.GraphLinksModel(nodes,links);
                $scope.model.selectedNodeData = null;
            }
        ]);
})();