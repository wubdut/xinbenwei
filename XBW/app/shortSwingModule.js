
define([], function() {

	var app = angular.module('shortSwingModule', []);
	
	app.controller('shortSwingCntrl', function($scope, $http) {
		$scope.shortSwingList = [];
        $http.get("http://localhost:8000/").then(function (response) {
        	// var str = JSON.stringify(response);
            $scope.shortSwingList = response.data;
        });
	});

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