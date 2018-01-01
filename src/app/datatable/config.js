// config.js

requirejs.config({

    baseUrl: '../../',

    paths: {
        angular: [
            // 'http://cdn.static.runoob.com/libs/angular.js/1.4.6/angular.min',
            'js/lib/angular.min'
        ],

        jquery: [
            'vendor/jquery/jquery.min'
        ],

        datatables: [
            'vendor/datatables/js/jquery.dataTables'
        ],

        datatablesBootstrap: [
            'vendor/datatables-plugins/dataTables.bootstrap'
        ],

        datatable: [
            'app/datatable/datatable'
        ],

        datatableModule: [
            'app/datatable/datatableModule'
        ]

    },

    map: {
        '*': {
            'css': 'js/lib/css.min'
        }
    },

    shim: {
        'datatable': {
            deps: [
                'datatableModule',
                'datatables',
                'css!vendor/bootstrap/css/bootstrap.min.css',
                'css!vendor/datatables-plugins/dataTables.bootstrap.css'
            ]
        },

        'datatables': {
            deps: ['jquery']
        },

        'datatableModule': {
            deps: ['angular']
        }

    },

    urlArgs: "bust=" +  (new Date()).getTime()

});

require(['datatable'], function() {
    angular.bootstrap(document, ['datatable']);
});

