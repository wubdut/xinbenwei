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
            }
        ]);
})();