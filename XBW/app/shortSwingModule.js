
define([], function() {

	var coreModule = angular.module('shortSwingModule', []);
	
	coreModule.controller('shortSwingCntrl', function($scope, $http) {
		$scope.shortSwingList = [];
        $http.get("http://39.108.214.220:8888/").then(function (response) {
        	// var str = JSON.stringify(response);
            $scope.shortSwingList = response.data;
        });
	});
});