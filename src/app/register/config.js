// config.js

requirejs.config({

    baseUrl: '../../',

    paths: {
        angular: [
            'http://apps.bdimg.com/libs/angular.js/1.4.6/angular.min',
            'js/lib/angular.min'
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

        // html5shiv: [
        //     'https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv',
        //     'vendor/ie/html5shiv'
        // ],
        //
        // respond: [
        //     'https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min',
        //     'vendor/ie/respond.min'
        // ],

        register: [
            'app/register/register'
        ],

        registerModule: [
            'app/register/registerModule'
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
        'register': {
            deps: [
                'registerModule',
                'bootstrap',
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
        },

        'authService': {
            deps: ['angular']
        }

    },

    urlArgs: "bust=" +  (new Date()).getTime()

});

require(['register'], function() {
    angular.bootstrap(document, ['register']);
});

