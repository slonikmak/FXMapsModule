class PolylineController extends PathController{

    addPolyline(latLngs, options){
        if (options === undefined){
            options = {}
        } else {
            options = JSON.parse(options);
        }
        options.bubblingMouseEvents = false;
        latLngs = JSON.parse(latLngs);
        const polyline = L.polyline(latLngs, options);
        console.log(options);
        this.mapGroup.addLayer(polyline);
        const id = this.getLayerId(polyline);
        this.registerEvents(id);
        return id;
    }

    toGeoJson(id){
        return this.getLayerById(id).toGeoJSON()
    }

    getLatLngs(id){
        return JSON.stringify(this.getLayerById(id).getLatLngs());
    }

    setLatLngs(id, latLngs){
        this.getLayerById(id).setLatLngs(latLngs)
    }

    isEmpty(id){
        return this.getLayerById(id).isEmpty()
    }

    addLatLng(id, latLng){
        this.getLayerById(id).addLatLng(latLng)
    }

    registerEvents(id){
        const layer = this.getLayerById(id);

        const events = {
            click: (e)=>{
                const event = new MapEvent('click', id, e.latlng);
                event.eventClass = 'MouseEvent';
                eventController.fireEven(event)
            }
        };

        layer.on(events)
    }

}
