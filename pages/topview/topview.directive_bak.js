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
                                        columnSpacing: 50,
                                        setsPortSpots: false })
                        }
                    );

                // define tooltips for nodes
                var tooltiptemplate =
                    $(go.Adornment, "Auto",
                        { background: "transparent" },
                        $(go.Shape, "Rectangle",
                          { fill: "whitesmoke", stroke: "whitesmoke" }
                          ),
                        $(go.Placeholder, { padding: 5 }),
                        $(go.TextBlock,
                            { alignment: go.Spot.Top, alignmentFocus: go.Spot.Bottom},
                            new go.Binding("text", "", function (node) {
                                return node.info;
                            }))
                    );

                // replace the default Node template in the nodeTemplateMap
                diagram.nodeTemplate =
                    $(go.Node, "Vertical",  // the whole node panel
                        { deletable: false, toolTip: tooltiptemplate, locationSpot: go.Spot.Center },
                        $(go.Panel, "Spot",
                            $(go.Panel, "Auto",
                                { name: "ICON" },
                                $(go.Shape,  //go.Shape 图形  有一些基础的圆 矩形 圆角矩形等 箭头
                                    { fill: null, portId: "",strokeWidth: 0,stroke: null },
                                    new go.Binding("background", "problem", nodeProblemConverter)),
                                $(go.Picture,  // the icon showing the logo
                                    // You should set the desiredSize (or width and height)
                                    // whenever you know what size the Picture should be.
                                    { desiredSize: new go.Size(80, NaN) },
                                    new go.Binding("source", "name", convertKeyImage)
                                ),
                                $(go.Shape, "Circle",
                                    { alignment: go.Spot.TopRight, alignmentFocus: go.Spot.TopRight,
                                        width: 10, height: 10, fill: "Green"},
                                    new go.Binding("fill", "alerts", nodeStatusConverter)
                                )
                            )
                        ),
                        $(go.TextBlock,  // the text label
                            { font: "bold 10pt helvetica, bold arial, sans-serif", textAlign: "center", maxSize: new go.Size(100, NaN), },
                            new go.Binding("text", "name")
                            // new go.Binding("stroke","strokeColor")
                        ),
                        {
                            click: function(e, obj) { window.selected_var=obj.part.data.type;showMessage(obj.part.data.type); }
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

                function nodeProblemConverter(msg) {
                    if (msg) return "red";
                    return null;
                }

                function nodeStatusConverter(s) {
                    if (s >= 2) return "red";
                    if (s >= 1) return "green";
                    return "green";
                }

                function convertKeyImage(name) {
                    if (!name) name = "JAVA";
                    return "img/topview-pictures/" + name + ".png";
                }

                function showMessage(s) {
                    self.location.href = "#/" + s;
                }

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

            }
        };
    }]);
})();