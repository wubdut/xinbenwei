
define(['datatablesBootstrap'], function(datatablesBootstrap) {

    var app = angular.module('datatableModule', []);

    app.controller('datatableCtl', function($scope, $http) {

        $('#example').dataTable();

        // $('#example').html("作用");

        // $scope.mytemplate = "template/recommend.html";

        // $(document).ready(function() {
        //     $('#dataTables-example').DataTable({
        //         responsive: true
        //     });
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