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

    getLength(id){
        const layer = this.getLayerById(id);
        const latlngs = layer.getLatLngs();
        let result = 0;
        for (let i=0;i<latlngs.length-1;i++){
            result += latlngs[i].distanceTo(latlngs[i+1]);
        }
        return result;
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
        //console.log(line.options);
        return JSON.stringify(obj);
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
