(function() {
    'use strict';
    /**
     * wub-neu
     * 2018.07.24
     * @group directive
     * @name goDiagram
     * @class
     */
    angular.module('inspinia')
        .directive('goDiagram', [function() {
            return {
                restrict: 'EA',
                template: 'pages/timepicker/timepicker.html',  // just an empty DIV element
                replace: true,
                controller: function ($scope) {
                    $scope.url = "lsdkjf";
                },
                link: function(scope, element, attrs) {



                }
            };
        }]);
})();