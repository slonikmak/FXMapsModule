class EditableController extends Controller{

    constructor(map, mapGroup) {
        super(map, mapGroup);

    }
    startPolyline(options){
        options  = JSON.parse(options);
        const line = this.map.editTools.startPolyline(null, options);
        this.mapGroup.addLayer(line);
        this.registerEvents(line);
        polyLineController.registerEvents(line);
        return line._leaflet_id;
    }

    startPolygon(options){
        options  = JSON.parse(options);
        const polygon = this.map.editTools.startPolygon(null, options);
        //log(polygon)
        this.mapGroup.addLayer(polygon);
        this.registerEvents(polygon);
        polygonController.registerEvents(polygon);
        return polygon._leaflet_id;
    }


    startMission(options){
        options = JSON.parse(options);
        const mission = this.map.editTools.startMissionPath(null, options);
        this.mapGroup.addLayer(mission);
        this.registerEvents(mission);
        missionController.registerEvents(mission);
        mission.on("mission:waypoint:new", (e)=>{
            console.log("new waypoint")
        });

        return mission._leaflet_id;
    }

    startMarker(){
        const marker = this.map.editTools.startMarker();
        this.registerEvents(marker);
        marker.on("editable:drawing:commit", (e)=>{
            this.mapGroup.addLayer(marker);
            markerController.registerEvents(marker._leaflet_id);
        });
        return marker._leaflet_id;
    }

    startCircle(options){
        options = JSON.parse(options);
        const circle = this.map.editTools.startCircle(null, options);
        //circleController.mapGroup.addLayer(circle);
        this.registerEvents(circle);
        circle.on("editable:drawing:commit", (e)=>{
            this.mapGroup.addLayer(circle);
            circleController.registerEvents(circle);
        });
        //
        return circle._leaflet_id;
    }


    registerEventsById(id){
        const layer = this.getLayerById(id);
        this.registerEvents(layer);
    }

    registerEvents(layer){
        const drawingEvents = ["editable:drawing:commit"];
        const vertexEvent = ["editable:vertex:dragend"];
        const editEvents = ["editable:editing"];

       /* const events = {
            "editable:drawing:commit": (e)=>{
                console.log(e);
                const event = new MapEvent('click', id, e.latlng);
                event.eventClass = "MouseEvent";
                eventController.fireEven(event)
            }
        }*/

       for (let i=0;i<drawingEvents.length;i++){
           layer.on(drawingEvents[i], (e)=>{
               const latlng = L.latLng(e.latlng.lat, e.latlng.lng);
               const event = new MapEvent(drawingEvents[i], layer._leaflet_id, latlng);
               event.eventClass = "EditableEvent";
               eventController.fireEven(event)
           });
       }
        for (let i=0;i<vertexEvent.length;i++){
            layer.on(vertexEvent[i], (e)=>{
                const latlng = e.vertex.latlng;
                //FIXME: сделать разные  объекты события
                const event = new MapEvent(vertexEvent[i], layer._leaflet_id, L.latLng(latlng.lat, latlng.lng));
                event.eventClass = "EditableEvent";
                eventController.fireEven(event)
            });
        }
        for (let i=0;i<editEvents.length;i++){
            layer.on(editEvents[i], (e)=>{
                let latlng = L.latLng(0.0,0.0);
                const event = new MapEvent(editEvents[i], layer._leaflet_id, latlng);
                event.eventClass = "EditableEvent";
                eventController.fireEven(event);
            });
        }

    }

    createTooltip(){
        console.log("create tooltip");
        const tooltip = L.DomUtil.get('tooltip');
        console.log(tooltip);

        function addTooltip (e) {
            if (e.layer instanceof L.Polyline){
                L.DomEvent.on(document, 'mousemove', moveTooltip);
                tooltip.innerHTML = 'Click on the map to start a line.';
                tooltip.style.display = 'block';
            }

        }

        function removeTooltip (e) {
            tooltip.innerHTML = '';
            tooltip.style.display = 'none';
            L.DomEvent.off(document, 'mousemove', moveTooltip);
        }

        function moveTooltip (e) {
            console.log("move tooltip")
            tooltip.style.left = e.clientX + 20 + 'px';
            tooltip.style.top = e.clientY - 10 + 'px';
        }

        function updateTooltip (e) {
            if (e.layer instanceof L.Polyline){
                if (e.layer.editor._drawnLatLngs.length < e.layer.editor.MIN_VERTEX) {
                    tooltip.innerHTML = 'Click on the map to continue line.';
                }
                else {
                    tooltip.innerHTML = 'Click on last point to finish line.';
                }
            }

        }
        this.map.on('editable:drawing:start', addTooltip);
        this.map.on('editable:drawing:end', removeTooltip);
        this.map.on('editable:drawing:click', updateTooltip);


    }
}
