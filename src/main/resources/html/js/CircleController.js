class CircleController extends PathController{

    addCircle(latLng, options){
        const circle = L.circle(JSON.parse(latLng), JSON.parse(options));
        this.mapGroup.addLayer(circle);

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
}

