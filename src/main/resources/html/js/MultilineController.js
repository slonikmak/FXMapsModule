class MultilineController extends PathController {
    toGeoJson(id) {
        const layer = this.getLayerById(id);
        const json = layer.toGeoJSON();
        //json.properties = layer.options;
        //json.properties.editOptions = {};
        console.log(json);
        return JSON.stringify(json);
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

    showMeasurements(id){
        this.getLayerById(id).showMeasurements();
    }

    hideMeasurements(id){
        this.getLayerById(id).hideMeasurements();
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

    removeLayer(id){
        console.log("remove!")
        const layer = this.map.findLayer(id);
        layer.remove();
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
        layer.on(events);
        layer.on("editable:editing", function (e) {
            layer.updateMeasurements();
        })
    }
}