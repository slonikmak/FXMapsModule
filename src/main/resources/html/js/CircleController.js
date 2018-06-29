class CircleController extends PathController{

    addCircle(latLng, options){
        console.log("add circle");
        const circle = L.circle(JSON.parse(latLng), JSON.parse(options));
        this.mapGroup.addLayer(circle);
        this.registerEvents(circle);
        return this.getLayerId(circle);
    }

    setRadius(id, radius){
        this.getLayerById(id).setRadius(radius)
    }

    getRadius(id){
        return this.getLayerById(id).getRadius()
    }

    toGeoJson(id){
        return this.getLayerById(id).toGeoJSON()
    }

    setLatLng(id, latLng){
        this.getLayerById(id).setLatLng(JSON.parse(latLng));
    }

    getLatLng(id){
        return this.getLayerById(id).getLatLng()
    }

    redraw(id, options){
        const circle = this.getLayerById(id);
        options = JSON.parse(options);
        console.log(options);
        for (let key in options){
            circle.options[key] = options[key]
        }
        circle.redraw();
    }

    setEditable(id, value){
        const circle = this.getLayerById(id);
        if (circle !== undefined){
            if (value) {
                console.log("enabled");
                circle.enableEdit();
            } else {
                console.log(circle);
                circle.disableEdit();
            }
        }
    }

    registerEventsById(id){
        this.registerEvents(this.getLayerById(id));
    }
    registerEvents(layer){
        const id = layer._leaflet_id;
        const mouseEvents = ["click"];
        for (let i = 0;i<mouseEvents.length;i++){
            layer.on(mouseEvents[i], (e)=>{
                const event = new MapEvent(mouseEvents[i], id, e.latlng);
                event.eventClass = 'MouseEvent';
                eventController.fireEven(event);
            })
        }
    }
}

