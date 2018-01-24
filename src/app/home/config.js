// config.js

requirejs.config({

    baseUrl: '../../',

    paths: {
        angular: [
            'http://apps.bdimg.com/libs/angular.js/1.4.6/angular.min',
            'js/lib/angular.min'
        ],

        home: [
            'app/home/home'
        ],

        homeModule: [
            'app/home/homeModule'
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
        'home': {
            deps: [
                'homeModule',
                'css!assets/css/bootstrap.css',
                'css!assets/css/bootstrap-responsive.css',
                'css!assets/css/docs.css'
            ]
        },

        'homeModule': {
            deps: ['angular']
        },

        'authService': {
            deps: ['angular']
        }

    },

    urlArgs: "bust=" +  (new Date()).getTime()

});

require(['home'], function() {
    angular.bootstrap(document, ['home']);
});

