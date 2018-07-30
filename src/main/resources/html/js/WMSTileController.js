class WMSTileController extends Controller{
    addTileLayer(url, options){
        console.log("add wms!!!!");
        options = JSON.parse(options);
        const tileLayer = L.tileLayer.wms(url, options);
        this.mapGroup.addLayer(tileLayer);
        const id = this.getLayerId(tileLayer);
        console.log("add tile to map "+url+" "+options+" id "+id);
        this.registerEvents(id);

        /*var nexrad = L.tileLayer.wms("http://mesonet.agron.iastate.edu/cgi-bin/wms/nexrad/n0r.cgi", {
            layers: 'nexrad-n0r-900913',
            format: 'image/png',
            transparent: true,
            attribution: "Weather data © 2012 IEM Nexrad"
        });*/

        //this.mapGroup.addLayer(tileLayer);
        tileLayer.addTo(this.map);

        return id;
    }
    setUrl(id,url){
        this.getLayerById(id).setUrl(url);
    }
    getUrl(id){
        return this.getLayerById(id).getTileUrl();
    }

    registerEvents(id){
        const layer = this.getLayerById(id);
        console.log(this);
        const events = {
            remove: (e)=>{
                const event = new MapEvent('remove', id, L.latLng(0,0));
                event.eventClass = "LayerEvent";
                eventController.fireEven(event)
            }
        };
        layer.on(events);
    }
}