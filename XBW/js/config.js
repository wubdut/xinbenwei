/**
 * INSPINIA - Responsive Admin Theme
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */
function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider, KeepaliveProvider) {

    // Configure Idle settings
    IdleProvider.idle(5); // in seconds
    IdleProvider.timeout(120); // in seconds

    $urlRouterProvider.otherwise("/manager/main");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    $stateProvider

        .state('register', {
            url: "/register",
            templateUrl: "views/register.html",
            data: { pageTitle: 'register' }
        })
        .state('login', {
            url: "/login",
            templateUrl: "views/login.html",
            data: { pageTitle: 'login' }
        })
        .state('myteam', {
            abstract: true,
            url: "/myteam",
            templateUrl: "views/common/content.html"
        })
        .state('myteam.main', {
            url: "/main",
            templateUrl: "views/table_data_myteam.html",
            data: { pageTitle: 'myteam' },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            serie: true,
                            files: ['js/plugins/dataTables/datatables.min.js','css/plugins/dataTables/datatables.min.css']
                        },
                        {
                            serie: true,
                            name: 'datatables',
                            files: ['js/plugins/dataTables/angular-datatables.min.js']
                        },
                        {
                            serie: true,
                            name: 'datatables.buttons',
                            files: ['js/plugins/dataTables/angular-datatables.buttons.min.js']
                        }
                    ]);
                }
            }
        })
        .state('myteam.add_member', {
            url: "/add_member",
            templateUrl: "views/add_member.html",
            data: { pageTitle: 'add member' }
        })
        .state('myteam.team_schedule', {
            url: "/team_schedule",
            templateUrl: "views/team_schedule.html",
            data: { pageTitle: 'team schedule' }
        })
        .state('manager', {
            abstract: true,
            url: "/manager",
            templateUrl: "views/common/content.html"
        })
        .state('manager.main', {
            url: "/main",
            templateUrl: "views/table_data.html",
            data: { pageTitle: 'departments' },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            serie: true,
                            files: ['js/plugins/dataTables/datatables.min.js','css/plugins/dataTables/datatables.min.css']
                        },
                        {
                            serie: true,
                            name: 'datatables',
                            files: ['js/plugins/dataTables/angular-datatables.min.js']
                        },
                        {
                            serie: true,
                            name: 'datatables.buttons',
                            files: ['js/plugins/dataTables/angular-datatables.buttons.min.js']
                        }
                    ]);
                }
            }
        })
        .state('manager.add_department', {
            url: "/add_department",
            templateUrl: "views/add_department.html",
            data: { pageTitle: 'add department' }
        })
        .state('manager.match_manager', {
            url: "/match_manager",
            templateUrl: "views/match_manager.html",
            data: { pageTitle: 'match manager' }
        })
        .state('manager.match_schedule_gn0', {
            url: "/match_schedule/0",
            templateUrl: "views/match_schedule.html",
            data: { pageTitle: 'match schedule' }
        })
        .state('manager.match_schedule_gn1', {
            url: "/match_schedule/1",
            templateUrl: "views/match_schedule.html",
            data: { pageTitle: 'match schedule' }
        })
        .state('manager.match_schedule_gn2', {
            url: "/match_schedule/2",
            templateUrl: "views/match_schedule.html",
            data: { pageTitle: 'match schedule' }
        })
        .state('manager.match_schedule_gn3', {
            url: "/match_schedule/3",
            templateUrl: "views/match_schedule.html",
            data: { pageTitle: 'match schedule' }
        });

}
angular
    .module('inspinia')
    .config(config)
    .run(function($rootScope, $state) {
        $rootScope.$state = $state;
    });
