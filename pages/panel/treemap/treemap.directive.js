(function() {
    'use strict';
    /**
     * wub-neu
     * 2018.07.30
     * @group directive
     * @name treemapDiagram
     * @class
     */
    angular.module('inspinia')
        .directive('treemapDiagram', [function() {
            return {
                restrict: 'E',
                templateUrl: 'pages/panel/treemap/treemap.html',
                replace: true,
                scope: true,
                controller: function ($scope, $http, $element, $attrs, AuthService) {
                    $scope.chartData = {};
                    $scope._id = "_id";

                    console.log("URL: " + AuthService.getURL() + $attrs.url);

                    $scope.height = {
                        height: $attrs.height
                    };

                    $scope.getData = function () {
                        $http.get(
                            AuthService.getURL() + $attrs.url
                            // {headers : authService.createAuthorizationTokenHeader()}
                        ).then(function (response) {
                            console.log(response.data);
                            $scope.chartData = response.data;
                            if (typeof($scope.chartData.title) !== "undefined"){
                                $scope._id = '_' + Math.random().toString(36).substr(2, 9);
                                $scope.title = $scope.chartData.title;
                            }

                        }, function () {
                            console.log("treemapDiagram no data");
                        });
                    };

                },
                link: function(scope, element, attrs) {

                    scope.getData();

                    scope.$watch('_id', function () {
                        // alert(scope._id);
                        // 基于准备好的dom，初始化echarts实例
                        var myChart = echarts.init(document.getElementById(scope._id));

                        // 指定图表的配置项和数据
                        var option = {
                            tooltip: {},
                            series: [{
                                name: 'root',
                                type: 'treemap',
                                top: 0,
                                bottom: 0,
                                left: 0,
                                right: 0,
                                visibleMin: 300,
                                data: scope.chartData.data,
                                leafDepth: 1,
                                levels: [
                                    {
                                        itemStyle: {
                                            normal: {
                                                borderColor: '#555',
                                                borderWidth: 4,
                                                gapWidth: 4
                                            }
                                        }
                                    },
                                    {
                                        colorSaturation: [0.3, 0.6],
                                        itemStyle: {
                                            normal: {
                                                borderColorSaturation: 0.7,
                                                gapWidth: 2,
                                                borderWidth: 2
                                            }
                                        }
                                    },
                                    {
                                        colorSaturation: [0.3, 0.5],
                                        itemStyle: {
                                            normal: {
                                                borderColorSaturation: 0.6,
                                                gapWidth: 1
                                            }
                                        }
                                    },
                                    {
                                        colorSaturation: [0.3, 0.5]
                                    }
                                ]
                            }]
                        };

                        // 使用刚指定的配置项和数据显示图表。
                        myChart.setOption(option);
                    });

                }
            };
        }]);
})();