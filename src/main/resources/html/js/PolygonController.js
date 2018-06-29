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
        this.registerEvents(id);
        return id;
    }

}