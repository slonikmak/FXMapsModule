(function () {

    L.MyEditable = L.Editable.extend({

       startMissionPath: function (options) {
            if (options === null || options === undefined){
                options={};
            }
            options.editorClass = L.MissionEditor;
            options.polylineClass = L.Mission;
            return this.startPolyline(null,options)
        }
    });

    L.Mission = L.Polyline.extend({
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
                //console.log("add vertex");
                const id =  e.vertex._leaflet_id;
                const circle = this.addCircle(e.latlng);
                e.vertex.circle = circle;
                e.vertex.on("move",(e)=>{
                    //console.log(e);
                    const event = new MissionEvent("mission:waypoint:move",id, L.latLng(e.latlng.lat, e.latlng.lng), "MissionEvent", id);
                    circle.setLatLng(e.latlng);
                    that.fire("mission:waypoint:move", event);
                });
                this.updateWaypoints();
                e.vertex.on("remove",(e)=>{
                    that.circles.removeLayer(e.sourceTarget.circle);
                });


                const event = new MissionEvent("mission:waypoint:new",id, L.latLng(e.latlng.lat, e.latlng.lng), "MissionEvent", id);
                //console.log("new event")
                //console.log(event)
                that.fire("mission:waypoint:new",event)
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
            //console.log(this);
            return new L.Waypoint(latlng, latlngs, this, opts || {});
        },
        /*addVertexMarkers: function (latlngs) {
            for (var i = 0, l = latlngs.length; i < l; i++) {
                this.addVertexMarker(latlngs[i], latlngs);
            }
        }*/
    });
    //console.log("init mission file");
})();

class MissionController extends MultilineController{
    addMission(latlngs, options){
        if (options === undefined){
            options = {}
        } else {
            options = JSON.parse(options);
        }
        //options.bubblingMouseEvents = false;
        latlngs = JSON.parse(latlngs);
        const mission = new L.Mission(latlngs, options);
        //console.log(options);
        //this.mapGroup.addLayer(polyline);
        const id = this.getLayerId(mission);
        this.registerEvents(mission);
        return id;
    }



    getLength(id){
        const layer = this.getLayerById(id);
        const latlngs = this.getLatLngs(layer._leaflet_id);
        let result = 0.0;
        for (let i=0;i<latlngs.length-1;i++){
            result += latlngs[i].distanceTo(latlngs[i+1]);
        }
        return result;
    }

    getDistance(id){

    }

    getIndex(id){

    }

    updateWaypoints(id, options){
        const layer = this.map.findLayer(id);
        options = JSON.parse(options);
        console.log(options);
        layer.circles.eachLayer((l)=>{
            //console.log(options);
            for (let key in options){
                l.options[key] = options[key]
                l.setRadius(options.radius);
            }
            l.redraw();
        })
    }

    getOptions(id){
        const line = this.getLayerById(id);
        const obj = {
            opacity: line.options.opacity,
            color: line.options.color,
            weight: line.options.weight,
            fill: line.options.fill,
            fillColor: line.options.fillColor,
            fillOpacity: line.options.fillOpacity,
            bubblingMouseEvents: line.options.bubblingMouseEvents,
            stroke: line.options.stroke,
            editable: line.editEnabled()
        };
        return JSON.stringify(obj);
    }

    registerEvents(layer){
        const missionEvents = ["mission:waypoint:new","mission:waypoint:move"];

        for (let i = 0; i < missionEvents.length; i++) {
            layer.on(missionEvents[i], (e)=>{
                const event = new MissionEvent(e.type, e.target._leaflet_id, e.latlng, e.eventClass, e.layer);
                //console.log("try to fire event");
                //console.log(event);
                eventController.fireEven(event);
            })
        }
    }


}