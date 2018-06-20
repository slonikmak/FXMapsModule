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
}
