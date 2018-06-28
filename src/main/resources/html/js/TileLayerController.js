class TileLayerController extends Controller{
    addTileLayer(url, options){
        const tileLayer = L.tileLayer(url, options);
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

        this.mapGroup.addLayer(nexrad);

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
        const events = {}
        layer.on(events);
    }
}

