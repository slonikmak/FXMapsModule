(function () {

    L.MyEditable = L.Editable.extend({

       startMissionPath: function (options) {
            if (options === null || options === undefined){
                options={};
            }
            options.editorClass = L.MissionEditor;
            options.polylineClass = L.CustomPolyline;
            return this.startPolyline(null,options)
        }
    });

    L.CustomPolyline = L.Polyline.extend({
        addCircle(latlng){
            const circle = new L.circle(latlng, {radius: this.options.radius});
            this.circles.addLayer(circle);
            return circle;
        },
        initialize(latlngs, options){
            const that = this;
            if (options === null || options === undefined){
                options = {}
            }
            options.radius = 50;
            this.circles = new L.layerGroup();
            // options = options || {};
            //options.editTools.vertexMarkerClass = L.Waypoint;
            L.Polyline.prototype.initialize.call(this, latlngs, options);
            this.on("editable:vertex:new", function (e) {
                console.log("add vertex");
                const circle = this.addCircle(e.latlng);
                e.vertex.circle = circle;
                e.vertex.on("move",(e)=>{
                    circle.setLatLng(e.latlng);
                });
                this.updateWaypoints();
                e.vertex.on("remove",(e)=>{
                    that.circles.removeLayer(e.sourceTarget.circle);
                });
                that.fire("mission:waypoint:new",{})
            });
        },
        onAdd: function (map) {
            //this._super.onAdd(map);
            //.log("add custom line!");
            //console.log("custom id " + this._leaflet_id);
            L.Polyline.prototype.onAdd.call(this, map);
            this.circles.addTo(map);
        },
        updateWaypoints() {
            const layers = this.editor.editLayer._layers;
            for (let key in layers) {
                if (layers[key] instanceof L.Waypoint) {
                    layers[key].updateIcon();
                }
            }
        }
    });

    L.Waypoint = L.Editable.VertexMarker.extend({

        iconHtml: function () {
            return '<div >' +
                '<div class="my-div-span" style="">' +
                '<div style="position: relative;top: -10px;left: -10px;width: 25px;height: 25px;background-color: white;border-radius: 50%;padding: 10px">'+this.numder +'</div>'+ '</div>';
        },

        initialize: function (latlng, latlngs, editor, options) {
            // We don't use this._latlng, because on drag Leaflet replace it while
            // we want to keep reference.
            this.latlng = latlng;
            const className = this.options.className;

            this.latlngs = latlngs;
            this.editor = editor;
            L.Marker.prototype.initialize.call(this, latlng, options);
            //this.options.icon = this.editor.tools.createVertexIcon({className: this.options.className});
            this.numder = this.getIndex();
            this.options.icon = new L.DivIcon({
                className: "my-div-span",
                html: this.iconHtml()
            });
            this.latlng.__vertex = this;
            this.editor.editLayer.addLayer(this);
            this.setZIndexOffset(editor.tools._lastZIndex + 1);
        },


        updateIcon() {
            this.numder = this.getIndex();
            this.setIcon(new L.DivIcon({
                className: "my-div-span",
                html: this.iconHtml()
            }));
            this.fire("mission:waypoint:update",{});
        }

    });

    L.MissionEditor = L.Editable.PolylineEditor.extend({
        addVertexMarker: function (latlng, latlngs, opts) {
            console.log(this);
            return new L.Waypoint(latlng, latlngs, this, opts || {});
        },
        /*addVertexMarkers: function (latlngs) {
            for (var i = 0, l = latlngs.length; i < l; i++) {
                this.addVertexMarker(latlngs[i], latlngs);
            }
        }*/
    });
    console.log("init mission file");
})();

class MissionController extends Controller{



}