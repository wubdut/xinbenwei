// config.js

requirejs.config({

    baseUrl: '../../',

    paths: {
        angular: [
            // 'http://cdn.static.runoob.com/libs/angular.js/1.4.6/angular.min',
            'js/lib/angular.min'
        ],

        angularDatatables: [
            'vendor/datatables/js/angular-dataTables.min'
        ],

        jquery: [
            // 'http://libs.baidu.com/jquery/2.1.1/jquery.min',
            'vendor/jquery/jquery.min'
        ],

        bootstrap: [
            'https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min',
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
            'https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv',
            'vendor/ie/html5shiv'
        ],

        respond: [
            'https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min',
            'vendor/ie/respond.min'
        ],

        accountModule: [
            'app/account/accountModule'
        ],

        authService: [
            'app/service/authService'
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
                'datatables',
                'angularDatatables',
                'accountModule',
                'html5shiv',
                'respond',
                'bootstrap',
                // 'dataTablesBootstrap',
                // 'dataTablesResponsive',
                'sb',
                'css!vendor/bootstrap/css/bootstrap.min.css',
                'css!vendor/metisMenu/metisMenu.min.css',
                'css!vendor/datatables-plugins/dataTables.bootstrap.css',
                'css!vendor/datatables-responsive/dataTables.responsive.css',
                'css!vendor/datatables/css/jquery.dataTables.css',
                'css!vendor/dist/css/sb-admin-2.css',
                'css!vendor/font-awesome/css/font-awesome.min.css',
                'css!vendor/js/price.css'
            ]
        },

        'angularDatatables': {
            deps: ['angular', 'jquery']
        },

        'authService': {
            deps: ['angular']
        },

        'accountModule': {
            deps: []
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

