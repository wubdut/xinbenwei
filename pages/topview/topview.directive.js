(function() {
    'use strict';
    /**
     * wub-neu
     * 2018.07.18
     * @group directive
     * @name goDiagram
     * @class
     */
    angular.module('inspinia')
        .directive('goDiagram', [function() {
        return {
            restrict: 'E',
            template: '<div></div>',  // just an empty DIV element
            replace: true,
            scope: { model: '=goModel' },
            controller: function ($scope) {
                $scope.path = {
                    TOMCAT: "TOMCAT",
                    SPRING_BOOT: "SPRING_BOOT",
                    APACHE: "APACHE",
                    ARCUS: "ARCUS",
                    BLOC: "BLOC",
                    CASSANDRA: "CASSANDRA",
                    CUBRID: "CUBRID",
                    DUBBO: "DUBOO",
                    ERROR: "ERROR",
                    JAVA: "JAVA",
                    JBOSS: "JBOSS",
                    JETTY: "JETTY",
                    MARIADB: "MARIADB",
                    MEMCACHED: "MEMCACHED",
                    MONGODB: "MONGODB",
                    MSSQLSERVER: "MSSQLSERVER",
                    MYSQL: "MYSQL",
                    MYSQL_EXECUTE_QUERY: "MYSQL",
                    NBASE: "NBASE",
                    NGINX: "NGINX",
                    ORACLE: "ORABLE",
                    POSTGRESQL: "POSTGRESQL",
                    QUEUE: "QUEUE",
                    REDIS: "REDIS",
                    RESIN: "RESIN",
                    STAND_ALONE: "STAND_ALONE",
                    UNAUTHORIZED: "UNAUTHORIZED",
                    UNDEFINED: "UNDEFINED",
                    VERTX: "VERTX",
                    WEBSPHERE: "WEBSPHERE",
                    USER: "USER"
                };
            },
            link: function(scope, element, attrs) {

                var $ = go.GraphObject.make;  // for conciseness in defining templates

                // Must name or refer to the DIV HTML element
                var diagram =
                    $(go.Diagram, element[0],
                        { // automatically scale the diagram to fit the viewport's size
                            initialAutoScale: go.Diagram.Uniform,
                            // start everything in the middle of the viewport
                            initialContentAlignment: go.Spot.Center,
                            // disable user copying of parts
                            allowCopy: false,
                            // position all of the nodes and route all of the links
                            layout:
                                $(go.LayeredDigraphLayout,
                                    { direction: 90,
                                        layerSpacing: 20,
                                        columnSpacing: 110,
                                        setsPortSpots: false })
                        }
                    );

                // replace the default Node template in the nodeTemplateMap
                diagram.nodeTemplate =
                    $(go.Node, "Vertical",  // the whole node panel
                        { deletable: false, locationSpot: go.Spot.Center },
                        $(go.Panel, "Spot",
                            $(go.Panel, "Auto",
                                { name: "ICON" },
                                $(go.Shape,  //go.Shape 图形  有一些基础的圆 矩形 圆角矩形等 箭头
                                    { fill: null, portId: "",strokeWidth: 0,stroke: null }
                                ),
                                $(go.Picture,  // the icon showing the logo
                                    // You should set the desiredSize (or width and height)
                                    // whenever you know what size the Picture should be.
                                    { desiredSize: new go.Size(80, 80) },
                                    new go.Binding("source", "type", convertKeyImage)
                                ),
                                $(go.Shape, "Circle",
                                    { alignment: go.Spot.TopRight, alignmentFocus: go.Spot.TopRight,
                                        width: 10, height: 10, fill: "Green"},
                                    new go.Binding("fill", "alerts", nodeStatusConverter)
                                )
                            )
                        ),
                        $(go.TextBlock,  // the text label
                            { font: "bold 10pt helvetica, bold arial, sans-serif", textAlign: "center", maxSize: new go.Size(100, NaN) },
                            new go.Binding("text", "key")
                            // new go.Binding("stroke","strokeColor")
                        ),
                        {
                            click: function(e, obj) { clickApp(obj.part.data.type, obj.part.data.key); }
                        }
                    );

                // replace the default Link template in the linkTemplateMap
                diagram.linkTemplate =
                    $(go.Link,  // the whole link panel
                        { curve: go.Link.Bezier, toShortLength: 2 },
                        $(go.Shape,  // the link shape
                            { strokeWidth: 1.5 }),
                        $(go.Shape,  // the arrowhead
                            { toArrow: "Standard", stroke: null }),
                        $(go.Panel, "Auto",
                            $(go.Shape,  // the label background, which becomes transparent around the edges
                                {
                                    fill: $(go.Brush, "Radial",
                                        { 0: "rgb(240, 240, 240)", 0.3: "rgb(240, 240, 240)", 1: "rgba(240, 240, 240, 0)" }),
                                    stroke: null
                                }),
                            $(go.TextBlock, "transition",  // the label text
                                {
                                    textAlign: "center",
                                    font: "10pt helvetica, arial, sans-serif",
                                    margin: 4,
                                    editable: true  // enable in-place editing
                                },
                                // editing the text automatically updates the model data
                                new go.Binding("text","flow").makeTwoWay())
                        )
                    );

                function nodeStatusConverter(s) {
                    if (s >= 1) return "red";
                    return "green";
                }

                function convertKeyImage(type) {
                    if (!type) type = "UNKNOWN";
                    // alert(scope.path[type]);
                    if (typeof(scope.path[type]) === "undefined") {
                        return "img/topview-pictures/" + "UNKNOWN" + ".png";
                    } else {
                        return "img/topview-pictures/" + scope.path[type] + ".png";
                    }
                }

                function clickApp(type, key) {
                    // alert(type + ": " + key);
                    if (typeof(scope.path[type]) === "undefined" || type === "USER") return;
                    // alert("#/rs/" + type + "/" + key);
                    self.location.href = "#/rs/" + type + "/" + key;
                }

                // notice when the value of "model" changes: update the Diagram.model
                scope.$watch("model", function(newmodel) {
                    var oldmodel = diagram.model;
                    if (oldmodel !== newmodel) {
                        diagram.removeDiagramListener("ChangedSelection", updateSelection);
                        diagram.model = newmodel;
                        diagram.addDiagramListener("ChangedSelection", updateSelection);
                    }
                });
                scope.$watch("model.selectedNodeData.name", function(newname) {
                    if (!diagram.model.selectedNodeData) return;
                    // disable recursive updates
                    diagram.removeModelChangedListener(updateAngular);
                    // change the name
                    diagram.startTransaction("change name");
                    // the data property has already been modified, so setDataProperty would have no effect
                    var node = diagram.findNodeForData(diagram.model.selectedNodeData);
                    if (node !== null) node.updateTargetBindings("name");
                    diagram.commitTransaction("change name");
                    // re-enable normal updates
                    diagram.addModelChangedListener(updateAngular);
                });

                // whenever a GoJS transaction has finished modifying the model, update all Angular bindings
                function updateAngular(e) {
                    if (e.isTransactionFinished) {
                        scope.$apply();
                    }
                }
                // update the Angular model when the Diagram.selection changes
                function updateSelection(e) {
                    diagram.model.selectedNodeData = null;
                    var it = diagram.selection.iterator;
                    while (it.next()) {
                        var selnode = it.value;
                        // ignore a selected link or a deleted node
                        if (selnode instanceof go.Node && selnode.data !== null) {
                            diagram.model.selectedNodeData = selnode.data;
                            break;
                        }
                    }
                    scope.$apply();
                }

            }
        };
    }]);
})();