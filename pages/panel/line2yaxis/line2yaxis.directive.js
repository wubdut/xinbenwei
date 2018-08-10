(function() {
    'use strict';
    /**
     * wub-neu
     * 2018.07.30
     * @group directive
     * @name lineDiagram
     * @class
     */
    angular.module('inspinia')
        .directive('line2yaxisDiagram', [function() {
            return {
                restrict: 'E',
                templateUrl: 'pages/panel/line/line.html',
                replace: true,
                scope: {
                    currentPeriod: "@"
                },
                controller: function ($scope, $http, $element, $attrs, AuthService) {
                    $scope._id = "_id";
                    $scope.title = "";
                    $scope.chartData = {};
                    $scope.c3Axis = {};
                    $scope.c3Data = {
                        columns: []
                    };

                    console.log("URL: " + AuthService.getURL() + $attrs.url);

                    $scope.getData = function (period) {
                        $http.get(
                            AuthService.getURL() + $attrs.url + period
                            // {headers : authService.createAuthorizationTokenHeader()}
                        ).then(function (response) {
                            console.log(response.data);
                            $scope.chartData = response.data;
                            if (typeof($scope.chartData.title) !== "undefined"){
                                $scope._id = '_' + Math.random().toString(36).substr(2, 9);
                                $scope.title = $scope.chartData.title;
                            }
                            $scope.c3Axis = transAxis($scope.chartData);
                            $scope.c3Data = transData($scope.chartData);
                        }, function () {
                            console.log("lineDiagram no data");
                        });
                    };

                    // $scope.getData('30m');

                    // $scope.chartData = {
                    //     title: "CPU Load",
                    //     yLabel: "Used(%)",
                    //     max: 500,
                    //     min: 0,
                    //     columns: [
                    //         {
                    //             key: 'x',
                    //             value: [1533009871000, 1533009872000, 1533009873000, 1533009874000, 1533009875000]
                    //         },
                    //         {
                    //             key: 'CPU',
                    //             value: [ 40, 100, 100, 400, 150, 250],
                    //             type: 'area'
                    //         },
                    //         {
                    //             key: 'CPU_2',
                    //             value: [ 30, 400, 10, 300, 250, 350],
                    //             type: 'area',
                    //             axes: 'y2'
                    //         }
                    //     ],
                    //     groups: [
                    //         ['CPU','CPU_2']
                    //     ]
                    // };

                    // if (typeof($scope.chartData.title) != "undefined"){
                    //     $scope._id = $scope.chartData.title.replace(" ", "_")+$scope._id;
                    //     $scope.title = $scope.chartData.title;
                    // }

                    // $scope.c3Axis = transAxis($scope.chartData);
                    // $scope.c3Data = transData($scope.chartData);

                    function transAxis(chartData) {
                        var res = {};
                        res['x'] = {
                            type: 'timeseries',
                            tick: {
                                format: '%H:%M:%S'
                            },
                            padding: {left:0, right:0}
                        };

                        var y = {}, y2 = {};
                        if (typeof(chartData.yLabel) !== "undefined") {
                            y['label'] = $scope.chartData.yLabel;
                        }
                        if (typeof(chartData.min) !== "undefined") {
                            y['min'] = $scope.chartData.min;
                        }
                        if (typeof(chartData.max) !== "undefined") {
                            y['max'] = $scope.chartData.max;
                        }
                        y['padding'] = {top:0, bottom:0};
                        res['y'] = y;

                        y2['show'] = true;
                        if (typeof(chartData.y2Label) !== "undefined") {
                            y2['label'] = $scope.chartData.y2Label;
                        }
                        if (typeof(chartData.min2) !== "undefined") {
                            y2['min'] = $scope.chartData.min2;
                        }
                        if (typeof(chartData.max2) !== "undefined") {
                            y2['max'] = $scope.chartData.max2;
                        }
                        y2['padding'] = {top:0, bottom:0};
                        res['y2'] = y2;
                        return res;
                    }

                    function transData(chartData) {
                        if (typeof(chartData.columns) === "undefined") {
                            return res;
                        }
                        var res = {
                            x: 'x',
                            columns: [],
                            types: {},
                            groups: [],
                            axes: {}
                        };
                        for (var i = 0; i < chartData.columns.length; i++) {
                            var tmp = [];
                            tmp.push(chartData.columns[i].key);
                            for (var j = 0; j < chartData.columns[i].value.length; j++) {
                                tmp.push(chartData.columns[i].value[j]);
                            }
                            res.columns.push(tmp);

                            if (typeof(chartData.columns[i].type) !== "undefined") {
                                res.types[chartData.columns[i].key] = chartData.columns[i].type;
                            }

                            if (typeof(chartData.columns[i].axes) !== "undefined" && chartData.columns[i].axes === 'y2') {
                                res.axes[chartData.columns[i].key] = 'y2';
                            }
                        }
                        if (typeof(chartData.groups) !== "undefined") {
                            res.groups = chartData.groups;
                        }
                        return res;
                    }
                },
                link: function(scope, element, attrs) {
                    scope.$watch('currentPeriod', function () {
                        if (scope.currentPeriod === "") return;
                        console.log("currentPeriod = " + scope.currentPeriod);
                        scope.getData(scope.currentPeriod);
                    });

                    scope.$watch('_id', function () {
                        c3.generate({
                            bindto: '#'+scope._id,
                            data: scope.c3Data,
                            axis: scope.c3Axis,
                            point: {
                                show: false
                            }
                        });
                    });
                }
            };
        }]);
})();