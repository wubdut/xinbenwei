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

        bootstrap: [
            'vendor/bootstrap/js/bootstrap.min'
        ],

        metisMenu: [
            'vendor/metisMenu/metisMenu.min'
        ],

        sb: [
            'vendor/dist/js/sb-admin-2'
        ],

        datatables: [
            'vendor/datatables/js/jquery.dataTables.min'
        ],

        dataTablesBootstrap: [
            'vendor/datatables-plugins/dataTables.bootstrap.min'
        ],

        dataTablesResponsive: [
            'vendor/datatables-responsive/dataTables.responsive'
        ],

        account: [
            'app/account/account'
        ],

        html5shiv: [
            // 'https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv',
            'vendor/ie/html5shiv'
        ],

        respond: [
            // 'https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min',
            'vendor/ie/respond.min'
        ],

        accountModule: [
            'app/account/accountModule'
        ]

    },

    map: {
        '*': {
            'css': 'js/lib/css.min'
        }
    },

    shim: {
        'account': {
            deps: [
                'accountModule',
                'html5shiv',
                'respond',
                'bootstrap',
                // 'jqueryDataTables',
                'dataTablesBootstrap',
                'dataTablesResponsive',
                'sb',
                'css!vendor/bootstrap/css/bootstrap.min.css',
                'css!vendor/metisMenu/metisMenu.min.css',
                'css!vendor/datatables-plugins/dataTables.bootstrap.css',
                'css!vendor/datatables-responsive/dataTables.responsive.css',
                'css!vendor/dist/css/sb-admin-2.css',
                'css!vendor/font-awesome/css/font-awesome.min.css'
            ]
        },

        'accountModule': {
            deps: ['angular']
        },

        'bootstrap': {
            deps: ['jquery']
        },

        'metisMenu': {
            deps: ['jquery']
        },

        'datatables': {
            deps: ['jquery']
        },

        'sb': {
            deps: ['metisMenu']
        }

    },

    urlArgs: "bust=" +  (new Date()).getTime()

});

require(['account'], function() {
    angular.bootstrap(document, ['account']);
});

