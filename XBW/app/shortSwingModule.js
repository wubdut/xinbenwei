
define([], function() {

	var coreModule = angular.module('shortSwingModule', []);
	
	coreModule.controller('shortSwingCntrl', function($scope, $http) {
		$scope.shortSwingList = [];
		for (var i = 0; i < 5; i++) {
            $scope.shortSwingList.push({
				id : '000001',
				name : '平安银行'
			});
		}
        $http.get("http://localhost:8888/").then(function (response) {
        	// var str = JSON.stringify(response);
            alert(response.data[0].content);
        });
	});
});