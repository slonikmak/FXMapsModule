class MultilineController extends PathController{
    toGeoJson(id){
        return this.getLayerById(id).toGeoJSON()
    }

    getLatLngs(id){
        const latlngs = this.getLayerById(id).getLatLngs();
        const result = [];
        for (let i =0;i<latlngs.length;i++){
            result[i] = [latlngs[i].lat, latlngs[i].lng]
        }
        return JSON.stringify(result);
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

    setEditable(id, value){
        const line = this.getLayerById(id);
        if (line !== undefined){
            if (value) {
                console.log("enabled");
                line.enableEdit();
            } else {
                console.log(line);
                line.disableEdit();
            }
        }
    }

    redraw(id, options){
        const line = this.getLayerById(id);
        options = JSON.parse(options);
        for (let key in options){
            line.options[key] = options[key]
        }
        line.redraw();
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