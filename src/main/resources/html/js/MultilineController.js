class MultilineController extends PathController {
    toGeoJson(id) {
        return this.getLayerById(id).toGeoJSON()
    }

    getLatLngs(id) {
        //FIXME:
        const result = [];
        const layer = this.map.findLayer(id);
        //console.log(layer instanceof L.Polygon)
        if (layer instanceof L.Polygon){
            console.log("get latlngs")
            const latlngs = layer.getLatLngs()[0];
            console.log(layer);
            for (let i = 0; i < latlngs.length; i++) {
                result[i] = [latlngs[i].lat, latlngs[i].lng]
            }
        }else if (layer instanceof L.Polyline){

            const latlngs = layer.getLatLngs();
            for (let i = 0; i < latlngs.length; i++) {
                result[i] = [latlngs[i].lat, latlngs[i].lng]
            }
        }
        return JSON.stringify(result);
    }

    setLatLngs(id, latLngs) {
        this.getLayerById(id).setLatLngs(latLngs)
    }

    isEmpty(id) {
        return this.getLayerById(id).isEmpty()
    }

    addLatLng(id, latLng) {
        this.getLayerById(id).addLatLng(latLng)
    }

    setEditable(id, value) {
        const line = this.getLayerById(id);
        if (line !== undefined) {
            if (value) {
                console.log("enabled");
                line.enableEdit();
            } else {
                console.log(line);
                line.disableEdit();
            }
        }
    }

    redraw(id, options) {
        console.log(id+" "+options);
        const line = this.getLayerById(id);
        console.log(line);
        options = JSON.parse(options);
        for (let key in options) {
            line.options[key] = options[key]
        }
        line.redraw();
    }

    registerEventsById(id) {
        const layer = this.getLayerById(id);
        this.registerEvents(layer);
    }

    registerEvents(layer) {
        const id = layer._leaflet_id;
        const events = {
            click: (e) => {
                const event = new MapEvent('click', id, e.latlng);
                event.eventClass = 'MouseEvent';
                eventController.fireEven(event)
            }
        };
        layer.on(events)
    }
}