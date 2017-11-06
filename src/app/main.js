// config.js

requirejs.config({
	// baseUrl: '',
	paths: {
		angular: [
			'http://cdn.static.runoob.com/libs/angular.js/1.4.6/angular.min',
			'../js/lib/angular.min'
		],

		bootstrap: [
			'https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min',
			'../js/lib/bootstrap.min'
		],

		app: 'app'
	},

	map: {
		'*': {
			'css': '../js/lib/css.min'
		}
	},
	
	shim: {
		'app': {
			deps: [
				'shortSwingModule',
				'css!../css/style.css',
				'css!https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css',
				// 'css!../css/bootstrap.min.css'
			]
		},

		'shortSwingModule': {
			deps: ['angular']
		}
	},

    urlArgs: "bust=" +  (new Date()).getTime()
	
});

require(['app'], function() {
	angular.bootstrap(document, ['app']);
});

