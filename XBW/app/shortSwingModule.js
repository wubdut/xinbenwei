
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
        // $http.get("http://10.0.67.14:8080/solr/metrics/select?indent=on&q=*:*&wt=json&rows=2").then(function (response) {
        //     alert(response.data.responseHeader.status);
        // });
	});
});