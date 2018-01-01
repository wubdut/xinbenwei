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

        html5shiv: [
            // 'https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv',
            'vendor/ie/html5shiv'
        ],

        respond: [
            // 'https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min',
            'vendor/ie/respond.min'
        ],

        register: [
            'app/register/register'
        ],

        registerModule: [
            'app/register/registerModule'
        ]

    },

    map: {
        '*': {
            'css': 'js/lib/css.min'
        }
    },

    shim: {
        'register': {
            deps: [
                'registerModule',
                'html5shiv',
                'respond',
                // 'jquery',
                'bootstrap',
                // 'metisMenu',
                'sb',
                'css!vendor/bootstrap/css/bootstrap.min.css',
                'css!vendor/metisMenu/metisMenu.min.css',
                'css!vendor/dist/css/sb-admin-2.css',
                'css!vendor/font-awesome/css/font-awesome.min.css'
            ]
        },

        'registerModule': {
            deps: ['angular']
        },

        'bootstrap': {
            deps: ['jquery']
        },

        'metisMenu': {
            deps: ['jquery']
        },

        'sb': {
            deps: ['metisMenu']
        }

    },

    urlArgs: "bust=" +  (new Date()).getTime()

});

require(['register'], function() {
    angular.bootstrap(document, ['register']);
});

