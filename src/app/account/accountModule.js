
define([''], function() {

    var app = angular.module('accountModule', []);

    app.controller('accountCtl', function($scope, $http) {

        $scope.mytemplate = "template/recommend.html";

        // $(document).ready(function() {
            $('#dataTables-example').DataTable({
                responsive: true
            });
        // });
        // $scope.loginSubmit = function () {
        //     var data = {
        //         email : $scope.formData.email,
        //         password : $scope.formData.password
        //     };
        //     alert(data.email);
            // $http.post('/login', data).success(function(response) {
            //     alert(response);
            // });
        // }

    });

});