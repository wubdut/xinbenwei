
define([], function() {

	var app = angular.module('shortSwingModule', []);
	
	app.controller('shortSwingCntrl', function($scope, $http) {
		$scope.shortSwingList = [];

		$scope.stockLink = function (stockId) {
            var link_str = "http://www.iwencai.com/stockpick/search?tid=stockpick&qs=box_main_ths&w=";
            return link_str + stockId;
        };

        $http.get("http://39.108.214.220:8000/").then(function (response) {   //39.108.214.220
        	// var str = JSON.stringify(response);
            $scope.shortSwingList = response.data;
        });


	});


    //http://www.iwencai.com/stockpick/search?tid=stockpick&qs=box_main_ths&w=
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