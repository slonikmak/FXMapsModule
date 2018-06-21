class EditableController extends Controller{

    constructor(map, mapGroup) {
        super(map, mapGroup);

    }
    startPolyline(){
        const line = this.map.editTools.startPolyline();
        this.mapGroup.addLayer(line);
        return line._leaflet_id;
    }

    startMarker(){
        const marker = this.map.editTools.startMarker();
        this.mapGroup.addLayer(marker);
        this.registerEvents(marker._leaflet_id);
        markerController.mapGroup.addLayer(marker);
        markerController.registerEvents(marker._leaflet_id);
        return marker._leaflet_id;
    }

    registerEvents(id){
        const layer = this.getLayerById(id);

        const events = ["editable:drawing:commit","editable:disable"];

       /* const events = {
            "editable:drawing:commit": (e)=>{
                console.log(e);
                const event = new MapEvent('click', id, e.latlng);
                event.eventClass = "MouseEvent";
                eventController.fireEven(event)
            }
        }*/

       for (let i=0;i<events.length;i++){
           layer.on(events[i], (e)=>{
               //console.log(e);
               const event = new MapEvent(events[i], id, e.latlng);
               event.eventClass = "EditableEvent";
               eventController.fireEven(event)
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
