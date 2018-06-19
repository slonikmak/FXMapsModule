class PolylineController extends PathController{

    addPolyline(latLngs, options){
        if (options === undefined){
            options = {}
        }
        options.bubblingMouseEvents = false;
        const polyline = L.polyline(latLngs, options);
        this.mainGroup.addLayer(polyline);
        const id = this.getLayerId(polyline);
        this.registerEvents(id);
        return id;
    }

    toGeoJson(id){
        return this.getLayerById(id).toGeoJSON()
    }

    getLatLngs(id){
        return this.getLayerById(id).getLatLngs()
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
                const event = new MapEvent('click', layer, e.latlng);
                eventController.fireEven(event)
            }
        };

        layer.on(events)
    }

}
