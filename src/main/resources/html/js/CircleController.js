class CircleController extends PathController{

    addCircle(latLng, options){
        console.log("add circle");
        const circle = L.circle(JSON.parse(latLng), JSON.parse(options));
        this.mapGroup.addLayer(circle);
        this.registerEvents(circle._leaflet_id);
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

    registerEvents(id){

    }
}

