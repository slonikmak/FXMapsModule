class PolygonController extends MultilineController{

    addPolygon(latLngs, options){
        console.log("add polygon");
        if (options === undefined){
            options = {}
        } else {
            options = JSON.parse(options);
        }
        //options.bubblingMouseEvents = false;
        latLngs = JSON.parse(latLngs);
        const polygon = L.polygon(latLngs, options);
        polygon.addTo(this.map);
        this.mapGroup.addLayer(polygon);
        this.registerEvents(polygon);
        return polygon._leaflet_id;
    }

    getLength(id){
        //FIXME: заменить везде поиск слоя и не добавлять слой в mapGroup, возможно сделать отдельную группу
        const layer = this.getLayerById(id);
        const latlngs = layer.getLatLngs()[0];
        let result = 0.0;
        for (let i=0;i<latlngs.length-1;i++){
            result += latlngs[i].distanceTo(latlngs[i+1]);
        }
        result+= latlngs[0].distanceTo(latlngs[latlngs.length-1]);
        return result;
    }

    getOptions(id){
        console.log("update");
        const polygon = this.map.findLayer(id);
        const obj = {
            opacity: polygon.options.opacity,
            color: polygon.options.color,
            weight: polygon.options.weight,
            fill: polygon.options.fill,
            fillColor: polygon.options.fillColor,
            fillOpacity: polygon.options.fillOpacity,
            bubblingMouseEvents: polygon.options.bubblingMouseEvents,
            stroke: polygon.options.stroke,
            editable: polygon.editEnabled()
        };
        console.log("obj");
        console.log(obj);
        return JSON.stringify(obj);
    }

}