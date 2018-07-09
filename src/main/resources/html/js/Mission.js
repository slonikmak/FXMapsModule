(function () {

    L.MyEditable = L.Editable.extend({

       startMissionPath: function (latlngs,options) {
            if (options === null || options === undefined){
                options={};
            }
            options.editorClass = L.MissionEditor;
            options.polylineClass = L.Mission;
            console.log("start mission");
            console.log(latlngs)
            return this.startPolyline(latlngs,options)
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
            options.editorClass = L.MissionEditor;
            options.polylineClass = L.Mission;
            options.radius = 50;
            this.circles = new L.layerGroup();
            // options = options || {};
            //options.editTools.vertexMarkerClass = L.Waypoint;
            L.Polyline.prototype.initialize.call(this, latlngs, options);
            //console.log(this);
            this.on("editable:vertex:new", function (e) {
                console.log("add vertex");
                //FIXME: перенести в один метод initWaypoints()
                const id =  e.vertex._leaflet_id;
                const circle = this.addCircle(e.latlng);
                const vertex = e.vertex;
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
                    /*const removeEvent = new MapEvent("remove", e.target._leaflet_id, L.latLng(e.latlng.lat, e.latlng.lng), "MissionEvent");
                    eventController.fireEven(removeEvent);*/
                });


                e.vertex.bindTooltip("<div>" +
                    "<span>Точка </span>"+e.vertex.getNumber()+"<br>" +
                    "<span>Дистанция до следующей</span>" +"<br>" +
                    "<span>Фиксация gps</span>"+"<br>" +
                    "<span>Курс</span>"+"<br>" +
                    "<span>Глубина погружения</span>"+"<br>" +
                    "<span>Глубина всплытия</span>"+"<br>" +
                    "</div>");

                const event = new MissionEvent("mission:waypoint:new",id, L.latLng(e.latlng.lat, e.latlng.lng), "MissionEvent", id);
                console.log("new event")
                //console.log(event)
                that.fire("mission:waypoint:new",event)
            });
            this.on("editable:vertex:deleted", (e)=>{
                const id =  e.vertex._leaflet_id;
                const event = new MissionEvent("mission:waypoint:new",id, L.latLng(e.latlng.lat, e.latlng.lng), "MissionEvent", id);
                this.updateWaypoints();
                that.fire("mission:waypoint:deleted",event);
            })

        },

        initWaypoints(){
            const that = this;
            const latlngs = this.getLatLngs();
            for (let i =0;i<latlngs.length;i++){
                const latlng = latlngs[i];
                const vertex = latlng.__vertex;
                const id =  vertex._leaflet_id;
                const circle = this.addCircle(latlng);
                vertex.circle = circle;
                vertex.on("move",(e)=>{
                    //console.log(e);
                    const event = new MissionEvent("mission:waypoint:move",id, L.latLng(latlng.lat, latlng.lng), "MissionEvent", id);
                    circle.setLatLng(e.latlng);
                    that.fire("mission:waypoint:move", event);
                });
                this.updateWaypoints();
                vertex.on("remove",(e)=>{
                    that.circles.removeLayer(e.sourceTarget.circle);
                });


                vertex.bindTooltip("<div>" +
                    "<span>Точка </span>"+vertex.getNumber()+"<br>" +
                    "<span>Дистанция до следующей</span>" +"<br>" +
                    "<span>Фиксация gps</span>"+"<br>" +
                    "<span>Курс</span>"+"<br>" +
                    "<span>Глубина погружения</span>"+"<br>" +
                    "<span>Глубина всплытия</span>"+"<br>" +
                    "</div>");
            }
        },

        onAdd: function (map) {
            //this._super.onAdd(map);
            //.log("add custom line!");
            //console.log("custom id " + this._leaflet_id);
            L.Polyline.prototype.onAdd.call(this, map);
            this.initWaypoints();
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
                '<div class="waypoint-icon" style="">'+this.numder +'</div>'+ '</div>';
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
        },

        getNumber(){
            return this.numder;
        }

    });

    L.MissionEditor = L.Editable.PolylineEditor.extend({
        customFunc(){

        },

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
        console.log("add mission!");
        console.log(options);

        if (options === undefined){
            options = {}
        } else {
            options = JSON.parse(options);
        }
        /*options.editOptions = {};
        options.editOptions.editorClass = L.MissionEditor;
        options.editOptions.vertexMarkerClass = L.Waypoint;*/
        //options.bubblingMouseEvents = false;
        //this.map.editTools = new L.MyEditable(this.map,{editorClass:L.MissionEditor, polylineClass:L.Mission});
        latlngs = JSON.parse(latlngs);
        const mission = new L.Mission(latlngs, options);
        //mission.editor = new L.MissionEditor();
        //const mission = this.map.editTools.startMissionPath(latlngs[0], options);
        //this.map.editTools.options.editorClass = L.MissionEditor;
        //this.map.editTools.options.vertexMarkerClass = L.Waypoint;
        mission.enableEdit(this.map);
        console.log(mission);
        /*for (let i = 1; i < latlngs.length; i++) {
            console.log("add vertex marker")
            mission.editor.addVertexMarker(latlngs[i]);
        }*/


        /*mission.editor.tools.options.vertexMarkerClass = L.Waypoint;*/
        mission.addTo(this.map);

        //this.map.editTools.stopDrawing();

        //mission.enableEdit(this.map);
        //console.log(options);
        this.mapGroup.addLayer(mission);
        //mission.addTo(this.map);
        const id = mission._leaflet_id;
        this.registerEvents(mission);
        editableController.registerEvents(mission);
        return id;
    }
    getWaypointIndex(id, waypointId){
        const layer = this.getLayerById(id);
        console.log("get waypoint index")
        const latlngs = layer.getLatLngs();
        console.log(latlngs);
        for (let i =0;i<latlngs.length;i++){
            if (latlngs[i].__vertex._leaflet_id == waypointId) {
                console.log("Number = "+latlngs[i].__vertex.getNumber());
                return latlngs[i].__vertex.getNumber();
            }
        }
    }

    getWaypointId(id, lat, lng){
        const layer = this.getLayerById(id);
        console.log("get")
        const latlngs = layer.getLatLngs();
        for (let i =0;i<latlngs.length;i++){
            if (latlngs[i].lat == lat && latlngs[i].lng == lng) {
                console.log("OKK");
                console.log(latlngs[i].__vertex._leaflet_id);
                return latlngs[i].__vertex._leaflet_id;
            }
        }

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
        const missionEvents = ["mission:waypoint:new","mission:waypoint:move","mission:waypoint:deleted"];

        const layerEvents = ["remove"];

        const id = layer._leaflet_id;

        for (let i = 0; i < missionEvents.length; i++) {
            layer.on(missionEvents[i], (e)=>{
                const event = new MissionEvent(e.type, e.target._leaflet_id, e.latlng, e.eventClass, e.layer);
                //console.log("try to fire event");
                console.log(e.type);
                eventController.fireEven(event);
            })
        }
        for (let i=0;i<layerEvents.length;i++){
            layer.on(layerEvents[i], (e)=>{
                const event = new MapEvent(layerEvents[i], id, L.latLng(0,0));
                event.eventClass = "LayerEvent";
                eventController.fireEven(event)
            })
        }
    }


}