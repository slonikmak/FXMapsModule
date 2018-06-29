class PolygonController extends MultilineController{
    addPolygone(latLngs, options){
        if (options === undefined){
            options = {}
        } else {
            options = JSON.parse(options);
        }
        //options.bubblingMouseEvents = false;
        latLngs = JSON.parse(latLngs);
        const polygon = L.polygon(latLngs, options);
        console.log(options);
        this.mapGroup.addLayer(polygon);
        const id = this.getLayerId(polygon);
        this.registerEvents(polygon);
        return id;
    }

    getLatLngs(id) {
        const layer = this.map.findLayer(id);
        const latlngs = layer.getLatLngs()[0];
        const result = [];
        for (let i = 0; i < latlngs.length; i++) {
            result[i] = [latlngs[i].lat, latlngs[i].lng]
        }
        return JSON.stringify(result);
    }
    getLength(id){
        //FIXME: заменить везде поиск слоя и не добавлять слой в mapGroup, возможно сделать отдельную группу
        const layer = this.map.findLayer(id);
        const latlngs = layer.getLatLngs()[0];
        let result = 0;
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