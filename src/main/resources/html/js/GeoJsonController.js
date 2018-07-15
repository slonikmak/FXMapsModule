class GeoJsonController extends Controller{
    addLayer(json){
        const obj = JSON.parse(json);
        const layer = L.geoJSON(obj, {
            style: function (feature) {
                return obj.properties;
                //return {color: "green"};
            }
        }).bindPopup(function (layer) {
            return "GeoJsonLayer"
            //return layer.feature.properties.description;
        }).addTo(this.map);

        this.registerEvents(layer);

        return layer._leaflet_id;
    }

    registerEvents(layer) {
        const id = layer._leaflet_id;
        const events = {
            click: (e) => {
                const event = new MapEvent('click', id, e.latlng);
                event.eventClass = 'MouseEvent';
                eventController.fireEven(event)
            },
            remove: (e)=>{
                const event = new MapEvent('remove', id, L.latLng(0,0));
                event.eventClass = "LayerEvent";
                eventController.fireEven(event)
            }
        };
        layer.on(events)
    }


}