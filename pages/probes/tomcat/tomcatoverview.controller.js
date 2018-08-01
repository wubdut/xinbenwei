(function() {
    'use strict';
    /**
     * wub-neu
     * 2018.08.01
     * @group Controller
     * @name TomcatoverviewCtrl
     * @class
     */
    angular.module('inspinia')
        .controller( 'TomcatoverviewCtrl', ['$scope', '$http', '$state', '$stateParams', '$window', 'AuthService',
            function ($scope, $http, $state, $stateParams, $window, authService) {
                // alert($stateParams.key);
                $scope.key = $stateParams.key;
            }
        ]);
})();