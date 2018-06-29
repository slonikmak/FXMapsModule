class PolylineController extends MultilineController{

    addPolyline(latLngs, options){
        if (options === undefined){
            options = {}
        } else {
            options = JSON.parse(options);
        }
        //options.bubblingMouseEvents = false;
        latLngs = JSON.parse(latLngs);
        const polyline = L.polyline(latLngs, options);
        console.log(options);
        this.mapGroup.addLayer(polyline);
        const id = this.getLayerId(polyline);
        this.registerEvents(polyline);
        return id;
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

}
