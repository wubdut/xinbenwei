/**
 * INSPINIA - Responsive Admin Theme
 *
 * Main controller.js file
 * Define controllers with data used in Inspinia theme
 *
 *
 * Functions (controllers)
 *  - MainCtrl
 *  - dashboardFlotOne
 *  - dashboardFlotTwo
 *  - dashboardFlotFive
 *  - dashboardMap
 *  - flotChartCtrl
 *  - rickshawChartCtrl
 *  - sparklineChartCtrl
 *  - widgetFlotChart
 *  - modalDemoCtrl
 *  - ionSlider
 *  - wizardCtrl
 *  - CalendarCtrl
 *  - chartJsCtrl
 *  - GoogleMaps
 *  - ngGridCtrl
 *  - codeEditorCtrl
 *  - nestableCtrl
 *  - notifyCtrl
 *  - translateCtrl
 *  - imageCrop
 *  - diff
 *  - idleTimer
 *  - liveFavicon
 *  - formValidation
 *  - agileBoard
 *  - draggablePanels
 *  - chartistCtrl
 *  - metricsCtrl
 *  - sweetAlertCtrl
 *  - selectCtrl
 *  - toastrCtrl
 *  - loadingCtrl
 *  - datatablesCtrl
 *  - truncateCtrl
 *  - touchspinCtrl
 *  - tourCtrl
 *  - jstreeCtrl
 *  - datamapsCtrl
 *  - pdfCtrl
 *	- departmentsCtrl
 *
 */

/**
 * MainCtrl - controller
 * Contains several global data used in different view
 *
 */

 var DEBUG = false;
 var UUID = "";
/**
 * MainCtrl - controller
 * Contains several global data used in different view
 *
 */
function MainCtrl($http) {

    /**
     * countries - Used as duallistbox in form advanced view
     */

    this.countries = [
        { name: 'Amsterdam' },
        { name: 'Washington' },
        { name: 'Sydney' },
        { name: 'Cairo' },
        { name: 'Beijing' }];

    /*this.getLocation = function(val) {
        return $http.get('//maps.googleapis.com/maps/api/geocode/json', {
            params: {
                address: val,
                sensor: false
            }
        }).then(function(response){
            return response.data.results.map(function(item){
                return item.formatted_address;
            });
        });
    };*/

    /**
     * daterange - Used as initial model for data range picker in Advanced form view
     */
    this.daterange = {startDate: null, endDate: null};

    /**
     * slideInterval - Interval for bootstrap Carousel, in milliseconds:
     */
    this.slideInterval = 5000;

    /**
     * tags - Used as advanced forms view in input tag control
     */

    this.tags = [
        { text: 'Amsterdam' },
        { text: 'Washington' },
        { text: 'Sydney' },
        { text: 'Cairo' },
        { text: 'Beijing' }
    ];

    /**
     * states - Data used in Advanced Form view for Chosen plugin
     */
    this.states = [
        'Alabama',
        'Alaska',
        'Arizona',
        'Arkansas',
        'California',
        'Colorado',
        'Connecticut',
        'Delaware',
        'Florida',
        'Georgia',
        'Hawaii',
        'Idaho',
        'Illinois',
        'Indiana',
        'Iowa',
        'Kansas',
        'Kentucky',
        'Louisiana',
        'Maine',
        'Maryland',
        'Massachusetts',
        'Michigan',
        'Minnesota',
        'Mississippi',
        'Missouri',
        'Montana',
        'Nebraska',
        'Nevada',
        'New Hampshire',
        'New Jersey',
        'New Mexico',
        'New York',
        'North Carolina',
        'North Dakota',
        'Ohio',
        'Oklahoma',
        'Oregon',
        'Pennsylvania',
        'Rhode Island',
        'South Carolina',
        'South Dakota',
        'Tennessee',
        'Texas',
        'Utah',
        'Vermont',
        'Virginia',
        'Washington',
        'West Virginia',
        'Wisconsin',
        'Wyoming'
    ];

    /**
     * check's - Few variables for checkbox input used in iCheck plugin. Only for demo purpose
     */
    this.checkOne = true;
    this.checkTwo = true;
    this.checkThree = true;
    this.checkFour = true;

    /**
     * knobs - Few variables for knob plugin used in Advanced Plugins view
     */
    this.knobOne = 75;
    this.knobTwo = 25;
    this.knobThree = 50;

    /**
     * Variables used for Ui Elements view
     */
    this.bigTotalItems = 175;
    this.bigCurrentPage = 1;
    this.maxSize = 5;
    this.singleModel = false;
    this.radioModel = 'Middle';
    this.checkModel = {
        left: false,
        middle: true,
        right: false
    };

    /**
     * groups - used for Collapse panels in Tabs and Panels view
     */
    this.groups = [
        {
            title: 'Dynamic Group Header - 1',
            content: 'Dynamic Group Body - 1'
        },
        {
            title: 'Dynamic Group Header - 2',
            content: 'Dynamic Group Body - 2'
        }
    ];

    /**
     * alerts - used for dynamic alerts in Notifications and Tooltips view
     */
    this.alerts = [
        { type: 'danger', msg: 'Oh snap! Change a few things up and try submitting again.' },
        { type: 'success', msg: 'Well done! You successfully read this important alert message.' },
        { type: 'info', msg: 'OK, You are done a great job man.' }
    ];

    /**
     * addAlert, closeAlert  - used to manage alerts in Notifications and Tooltips view
     */
    this.addAlert = function() {
        this.alerts.push({msg: 'Another alert!'});
    };

    this.closeAlert = function(index) {
        this.alerts.splice(index, 1);
    };

    /**
     * randomStacked - used for progress bar (stacked type) in Badges adn Labels view
     */
    this.randomStacked = function() {
        this.stacked = [];
        var types = ['success', 'info', 'warning', 'danger'];

        for (var i = 0, n = Math.floor((Math.random() * 4) + 1); i < n; i++) {
            var index = Math.floor((Math.random() * 4));
            this.stacked.push({
                value: Math.floor((Math.random() * 30) + 1),
                type: types[index]
            });
        }
    };
    /**
     * initial run for random stacked value
     */
    this.randomStacked();

    /**
     * summernoteText - used for Summernote plugin
     */
    this.summernoteText = ['<h3>Hello Jonathan! </h3>',
    '<p>dummy text of the printing and typesetting industry. <strong>Lorem Ipsum has been the dustrys</strong> standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more',
        'recently with</p>'].join('');

    /**
     * General variables for Peity Charts
     * used in many view so this is in Main controller
     */
    this.BarChart = {
        data: [5, 3, 9, 6, 5, 9, 7, 3, 5, 2, 4, 7, 3, 2, 7, 9, 6, 4, 5, 7, 3, 2, 1, 0, 9, 5, 6, 8, 3, 2, 1],
        options: {
            fill: ["#1ab394", "#d7d7d7"],
            width: 100
        }
    };

    this.BarChart2 = {
        data: [5, 3, 9, 6, 5, 9, 7, 3, 5, 2],
        options: {
            fill: ["#1ab394", "#d7d7d7"]
        }
    };

    this.BarChart3 = {
        data: [5, 3, 2, -1, -3, -2, 2, 3, 5, 2],
        options: {
            fill: ["#1ab394", "#d7d7d7"]
        }
    };

    this.LineChart = {
        data: [5, 9, 7, 3, 5, 2, 5, 3, 9, 6, 5, 9, 4, 7, 3, 2, 9, 8, 7, 4, 5, 1, 2, 9, 5, 4, 7],
        options: {
            fill: '#1ab394',
            stroke: '#169c81',
            width: 64
        }
    };

    this.LineChart2 = {
        data: [3, 2, 9, 8, 47, 4, 5, 1, 2, 9, 5, 4, 7],
        options: {
            fill: '#1ab394',
            stroke: '#169c81',
            width: 64
        }
    };

    this.LineChart3 = {
        data: [5, 3, 2, -1, -3, -2, 2, 3, 5, 2],
        options: {
            fill: '#1ab394',
            stroke: '#169c81',
            width: 64
        }
    };

    this.LineChart4 = {
        data: [5, 3, 9, 6, 5, 9, 7, 3, 5, 2],
        options: {
            fill: '#1ab394',
            stroke: '#169c81',
            width: 64
        }
    };

    this.PieChart = {
        data: [1, 5],
        options: {
            fill: ["#1ab394", "#d7d7d7"]
        }
    };

    this.PieChart2 = {
        data: [226, 360],
        options: {
            fill: ["#1ab394", "#d7d7d7"]
        }
    };
    this.PieChart3 = {
        data: [0.52, 1.561],
        options: {
            fill: ["#1ab394", "#d7d7d7"]
        }
    };
    this.PieChart4 = {
        data: [1, 4],
        options: {
            fill: ["#1ab394", "#d7d7d7"]
        }
    };
    this.PieChart5 = {
        data: [226, 134],
        options: {
            fill: ["#1ab394", "#d7d7d7"]
        }
    };
    this.PieChart6 = {
        data: [0.52, 1.041],
        options: {
            fill: ["#1ab394", "#d7d7d7"]
        }
    };
};

/**
 * translateCtrl - Controller for translate
 */
function translateCtrl($translate, $scope) {
    $scope.changeLanguage = function (langKey) {
        $translate.use(langKey);
        $scope.language = langKey;
    };
}

function departmentsCtrl($scope, $http, $location, DTOptionsBuilder){
	$scope.departments = [];
	$http.get('http://localhost:8082/department/allDepartment?uuid=' + UUID).success(function(data){
        if (data.code !== '200') {
            $location.path("login");
        } else {
            $scope.departments = eval('(' + data.msg + ')');
        }
	});

    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withDOM('<"html5buttons"B>lTfgitp')
        .withButtons([
            {extend: 'copy'},
            {extend: 'csv'},
            {extend: 'excel', title: 'ExampleFile'},
            {extend: 'pdf', title: 'ExampleFile'},

            {extend: 'print',
                customize: function (win){
                    $(win.document.body).addClass('white-bg');
                    $(win.document.body).css('font-size', '10px');
                    $(win.document.body).find('table')
                        .addClass('compact')
                        .css('font-size', 'inherit');
                }
            }
        ]);

    $scope.del_department = function(department) {
        $http.get('http://localhost:8082/department/delDepartment?id=' + department.id + '&uuid=' + UUID).success(function(data){
            $http.get('http://localhost:8082/department/allDepartment?uuid=' + UUID).success(function(data){
                if (data.code !== '200') {
                    $location.path("login");
                } else {
                    $scope.departments = eval('(' + data.msg + ')');
                }
            });
        });
    };
}

function teamsCtrl($scope, $http, DTOptionsBuilder){

    $scope.teams = [];
    $http.get('http://localhost:8082/team/allTeam/').success(function(data){
        $scope.teams = data;
    });

    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withDOM('<"html5buttons"B>lTfgitp')
        .withButtons([
            {extend: 'copy'},
            {extend: 'csv'},
            {extend: 'excel', title: 'ExampleFile'},
            {extend: 'pdf', title: 'ExampleFile'},

            {extend: 'print',
                customize: function (win){
                    $(win.document.body).addClass('white-bg');
                    $(win.document.body).css('font-size', '10px');
                    $(win.document.body).find('table')
                        .addClass('compact')
                        .css('font-size', 'inherit');
                }
            }
        ]);

    $scope.del_team = function(team) {
        $http.get('http://localhost:8082/team/del_team?name=' + department.id).success(function(data){
            $http.get('http://localhost:8082/team/allTeam/').success(function(data){
                $scope.departments = data;
            });
        });
    };
}


function adddepartmentCtrl($scope, $http){
    $scope.add_department = function () {
        $http.get('http://localhost:8082/department/addDepartment?name=' + $scope.department_name + "&uuid=" + UUID).success(function(data){
            if ( data.code === 200 ) alert("创建部门成功");
            else alert('创建部门失败');
        });
    };
}

function addmemberCtrl($scope, $http, $location){
    $scope.add_member = function () {
        $http.get('http://localhost:8082/team/addMember?name=' + $scope.name + '&email=' + $scope.email + '&phone='+$scope.tel + '&uuid=' + UUID).success(function(data){
            if (data.code !== '200') {
                $location.path("login");
            } else {
                $location.path("myteam/main");
            }
        });
    };
}

function membersCtrl($scope, $http, $location, DTOptionsBuilder) {
    $scope.members = [];
    $http.get('http://localhost:8082/team/allMember?uuid=' + UUID).success(function(data){
        if (data.code !== '200') {
            $location.path("login");
        } else {
            $scope.members = eval('(' + data.msg + ')');
        }
    });

    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withDOM('<"html5buttons"B>lTfgitp')
        .withButtons([
            {extend: 'copy'},
            {extend: 'csv'},
            {extend: 'excel', title: 'ExampleFile'},
            {extend: 'pdf', title: 'ExampleFile'},

            {extend: 'print',
                customize: function (win){
                    $(win.document.body).addClass('white-bg');
                    $(win.document.body).css('font-size', '10px');
                    $(win.document.body).find('table')
                        .addClass('compact')
                        .css('font-size', 'inherit');
                }
            }
        ]);
}

function teamscheduleCtrl($scope, $http, $location) {
    function getInititalData(year) {
        year = year || '2017';
        var date = +echarts.number.parseDate(year + '-09-01');
        var end = +echarts.number.parseDate((+year) + '-10-30');
        var dayTime = 3600 * 24 * 1000;
        var data = [];
        for (var time = date; time <= end; time += dayTime) {
            data.push([
                echarts.format.formatTime('yyyy-MM-dd', time),30000
            ]);
        }
        return data;
    }
    var data = getInititalData(2017);

    var option = {
        backgroundColor: '#404a59',
        calendar: [{
            top: '15%',
            bottom: '15%',
            right: '14%',
            left: '20%',
            range: ['2017-09-01', '2017-10-30'],
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#000',
                    width: 4,
                    type: 'solid'
                }
            },
            yearLabel: {
                formatter: '',
                show:false, 
                textStyle: {
                    color: '#404a59'
                }
            },
            itemStyle: {
                normal: {
                    color: '#323c48',
                    borderWidth: 1,
                    borderColor: '#111'
                }
            }
        }],
        series : [
            {
                type: 'scatter',
                coordinateSystem: 'calendar',
                data: data,
                itemStyle: {
                    normal: {
                        color: '#22f925'
                    }
                }
            },
            {
                type: 'effectScatter',
                coordinateSystem: 'calendar',
                calendarIndex: 0,
                data: [],
                symbolSize: function (val) {
                    return val[1] / 500;
                },
                showEffectOn: 'render',
                rippleEffect: {
                    brushType: 'stroke'
                },
                hoverAnimation: true,
                itemStyle: {
                    normal: {
                        color: '#f92225',
                        shadowBlur: 10,
                        shadowColor: '#333'
                    }
                },
            }
        ]
    };
    $scope.option = option;
    $scope.uuid = UUID;
}

function registerCtrl($scope, $http, $location){
    $scope.nameerror = undefined;
    $scope.departments = [];
    $scope.choosenItem = undefined;
    $http.get('http://localhost:8082/department/allDepartment/').success(function(data){
        $scope.departments = data;
        $scope.choosenItem = data[0].department;
    });
    $scope.register = function () {
        $http.get('http://localhost:8082/register?name=' + $scope.name + '&password=' + $scope.password + '&department=' + $scope.choosenItem).success(function(data){
            if (data.code !== '200') {
                $scope.nameerror = 1;
            } else {
                UUID = data.msg;
                $location.path("myteam/main");
            }
        });
    };
}

function loginCtrl($scope, $http, $location){
    $scope.nameerror = undefined;
    $scope.login = function () {
        $http.get('http://localhost:8082/login?name=' + $scope.name + '&password=' + $scope.password).success(function(data){
            if (data.code !== '200') {
                $scope.nameerror = 1;
            } else {
                UUID = data.msg;
                $location.path("myteam/main");
            }
        });
    };
}

function matchscheduleCtrl($scope, $http, $location){
    var gn = $location.url().split('/')[3];
    $http.get('http://localhost:8082/competition/groupSchedule?gn=' + gn + "&uuid=" + UUID).success(function(data){
        console.log(data);
        if (data.code !== '200') {
            $location.path("login");
        } else {
            $scope.schedules = eval('(' + data.msg + ')');
        }
    });
}

function team_scheduleCtrl($scope, $http, $location){
    
    $http.get('http://localhost:8082/team/teamSchedule?uuid=' + UUID).success(function(data){
        $scope.schedules = eval('(' + data.msg + ')');
    });
}

function schedulesCtrl($scope, $http, DTOptionsBuilder){
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withDOM('<"html5buttons"B>lTfgitp')
        .withButtons([
            {extend: 'copy'},
            {extend: 'csv'},
            {extend: 'excel', title: 'ExampleFile'},
            {extend: 'pdf', title: 'ExampleFile'},

            {extend: 'print',
                customize: function (win){
                    $(win.document.body).addClass('white-bg');
                    $(win.document.body).css('font-size', '10px');
                    $(win.document.body).find('table')
                        .addClass('compact')
                        .css('font-size', 'inherit');
                }
            }
        ]);

    $http.get('http://localhost:8082/competition/allSchedule').success(function(data){
        $scope.schedules = eval('(' + data.msg + ')');
    });
}

function matchmanagerCtrl($scope, $http, $location){
    $scope.update_schedule = function(cn) {
        if (undefined === cn) {
            cn = 1;
        }
        $http.get('http://localhost:8082/competition/updateSchedule?gn=0&cn=' + cn + "&uuid=" + UUID).success(function(data){
            if (data.code !== '200') {
                $location.path("login");
            } else {
                $scope.a = eval('(' + data.msg + ')');
            }
        });
        $http.get('http://localhost:8082/competition/updateSchedule?gn=1&cn=' + cn + "&uuid=" + UUID).success(function(data){
            if (data.code !== '200') {
                $location.path("login");
            } else {
                $scope.b = eval('(' + data.msg + ')');
            }
        });
        $http.get('http://localhost:8082/competition/updateSchedule?gn=2&cn=' + cn + "&uuid=" + UUID).success(function(data){
            if (data.code !== '200') {
                $location.path("login");
            } else {
                $scope.c = eval('(' + data.msg + ')');
            }
        });
        $http.get('http://localhost:8082/competition/updateSchedule?gn=3&cn=' + cn + "&uuid=" + UUID).success(function(data){
            if (data.code !== '200') {
                $location.path("login");
            } else {
                $scope.d = eval('(' + data.msg + ')');
            }
        });
        $http.get('http://localhost:8082/competition/groupScheduleGallery').success(function(data){
            $scope.data = eval('(' + data.msg + ')');
            var option = {
                tooltip: {
                    trigger: 'item',
                    triggerOn: 'mousemove'
                },
                series: [
                    {
                        type: 'sankey',
                        layout: 'none',
                        data: $scope.data.nodes,
                        links: $scope.data.links,
                        itemStyle: {
                            normal: {
                                borderWidth: 1,
                                borderColor: '#aaa'
                            }
                        },
                        lineStyle: {
                            normal: {
                                color: 'source',
                                curveness: 0.5
                            }
                        }
                    }
                ]
            };
            $scope.option = option;
        });
    };

    $scope.update_schedule(1);
}


function importantCtrl($scope, $http, $location){
    $scope.update = function() {
        $http.get('http://localhost:8082/competition/important').success(function(data){
            $scope.data = eval('(' + data.msg + ')');
            // Generate data
            var category = [];
            var dottedBase = +new Date();
            var barData = [];

            for (var o in $scope.data) {
                category.push($scope.data[o].key);
                barData.push($scope.data[o].value);
            }

            // option
            var option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                xAxis: {
                    data: category,
                    axisLine: {
                        lineStyle: {
                            color: '#ccc'
                        }
                    }
                },
                yAxis: {
                    splitLine: {show: false},
                    axisLine: {
                        lineStyle: {
                            color: '#ccc'
                        }
                    }
                },
                series: [ {
                    name: '不能参加战队数',
                    type: 'bar',
                    barWidth: 10,
                    itemStyle: {
                        normal: {
                            barBorderRadius: 5,
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#14c8d4'},
                                    {offset: 1, color: '#43eec6'}
                                ]
                            )
                        }
                    },
                    data: barData
                }]
            };
            $scope.option = option;
        });
    };

    $scope.update();
}

function departmentnumberCtrl($scope, $http, $location) {
    $http.get('http://localhost:8082/team/departmentTeamNumber').success(function(data){
        $scope.data = eval('(' + data.msg + ')');
        var legend_data = [];
        for(var o in $scope.data){
            legend_data.push($scope.data[o].name);
        }
        var option = {
            tooltip: {
                trigger: 'item',
                formatter: "{b}: {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                data:legend_data
            },
            series: [
                {
                    name:'',
                    type:'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: false,
                    label: {
                        normal: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                fontSize: '30',
                                fontWeight: 'bold'
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    data:$scope.data
                }
            ]
        };

        $scope.option1 = option;
    });

    $http.get('http://localhost:8082/team/departmentMemberNumber').success(function(data){
        $scope.data = eval('(' + data.msg + ')');
        var legend_data = [];
        for(var o in $scope.data){
            legend_data.push($scope.data[o].name);
        }
        var option = {
            tooltip: {
                trigger: 'item',
                formatter: "{b}: {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                data:legend_data
            },
            series: [
                {
                    name:'',
                    type:'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: false,
                    label: {
                        normal: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                fontSize: '30',
                                fontWeight: 'bold'
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    data:$scope.data
                }
            ]
        };

        $scope.option2 = option;
    });
}

function lastCtrl($scope, $http, $location) {
    $http.get('http://localhost:8082/team/last?uuid=' + UUID).success(function(data){
        $scope.groups = eval('(' + data.msg + ')');
        console.log($scope.groups);
    });
}


/**
 *
 * Pass all functions into module
 */
angular
    .module('inspinia')
    .controller('MainCtrl', MainCtrl)
    .controller('translateCtrl', translateCtrl)
    .controller('departmentsCtrl', departmentsCtrl)
    .controller('adddepartmentCtrl', adddepartmentCtrl)
    .controller('registerCtrl', registerCtrl)
    .controller('loginCtrl', loginCtrl)
    .controller('teamsCtrl', teamsCtrl)
    .controller('addmemberCtrl', addmemberCtrl)
    .controller('membersCtrl', membersCtrl)
    .controller('teamscheduleCtrl', teamscheduleCtrl)
    .controller('matchscheduleCtrl', matchscheduleCtrl)
    .controller('schedulesCtrl', schedulesCtrl)
    .controller('matchmanagerCtrl', matchmanagerCtrl)
    .controller('departmentnumberCtrl', departmentnumberCtrl)
    .controller('importantCtrl', importantCtrl)
    .controller('team_scheduleCtrl', team_scheduleCtrl)
    .controller('lastCtrl', lastCtrl);
