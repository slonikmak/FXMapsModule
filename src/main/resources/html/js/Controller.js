class Controller{
    constructor(map){
        this.mainGroup = L.layerGroup();
        this.map = map;
        this.mainGroup.addTo(this.map)
    }

    getLayerId(layer){
        return this.mainGroup.getLayerId(layer)
    }

    getLayerById(id){
        return this.mainGroup.getLayer(id)
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

