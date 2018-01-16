// config.js

requirejs.config({

    baseUrl: '../../',

    paths: {
        angular: [
            'http://apps.bdimg.com/libs/angular.js/1.4.6/angular.min',
            'js/lib/angular.min'
        ],

        angularDatatables: [
            'vendor/datatables/js/angular-datatables.min'
        ],

        jquery: [
            'https://cdn.bootcss.com/jquery/3.1.1/jquery.min',
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
            // 'http://cdn.datatables.net/1.10.15/js/jquery.dataTables.min',
            'vendor/datatables/js/jquery.dataTables.min'
        ],

        dataTablesBootstrap: [
            'vendor/datatables-plugins/dataTables.bootstrap.min'
        ],

        // dataTablesResponsive: [
        //     'vendor/datatables-responsive/dataTables.responsive'
        // ],

        account: [
            'app/account/account'
        ],

        // html5shiv: [
        //     'https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv',
        //     'vendor/ie/html5shiv'
        // ],
        //
        // respond: [
        //     'https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min',
        //     'vendor/ie/respond.min'
        // ],

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
                'bootstrap',
                'dataTablesBootstrap',
                // 'dataTablesResponsive',
                'sb',
                'css!vendor/bootstrap/css/bootstrap.min.css',
                'css!vendor/metisMenu/metisMenu.min.css',
                'css!vendor/datatables-plugins/dataTables.bootstrap.css',
                // 'css!vendor/datatables-responsive/dataTables.responsive.css',
                'css!vendor/datatables/css/jquery.dataTables.css',
                // 'css!http://cdn.datatables.net/1.10.15/css/jquery.dataTables.min.css',
                'css!vendor/dist/css/sb-admin-2.css',
                'css!vendor/font-awesome/css/font-awesome.min.css',
                'css!vendor/css/price.css'
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

