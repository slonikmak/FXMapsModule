class Controller{
    constructor(map, mapGroup){
        //this.mainGroup = L.layerGroup();
        this.map = map;
        //this.mainGroup.addTo(this.map);
        this.mapGroup = mapGroup;
        //mapGroup.addLayer(this.mainGroup);
    }

    getLayerId(layer){
        return this.mapGroup.getLayerId(layer);
    }

    getLayerById(id){
        return this.mapGroup.getLayer(id);
    }


    remove(id){
        this.getLayerById(id).remove()
    }

    bindPopup(id, text){
        this.getLayerById(id).bindPopup(text)
    }

    openPopup(id, latLng){
        this.getLayerById(id).openPopup(latLng)
    }

    closePopup(id){
        this.getLayerById(id).closePopup()
    }

}

