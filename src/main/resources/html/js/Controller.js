class Controller{
    constructor(map, mapGroup){
        //this.mainGroup = L.layerGroup();
        this.map = map;
        //this.mainGroup.addTo(this.map);
        this.mapGroup = mapGroup;
        //mapGroup.addLayer(this.mainGroup);
    }

    getLayerId(layer){
        return layer._leaflet_id;
    }

    getLayerById(id){
        //return this.map.findLayer(id);
        return this.mapGroup.getLayer(id);
    }

    removeLayer(id){
        const layer = this.map.findLayer(id);
        layer.remove();
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

    //TODO: регистрация стандартных событий Leaflet
    registerEvents(layer){

    }

}

