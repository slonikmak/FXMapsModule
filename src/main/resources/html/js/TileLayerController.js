class TileLayerController extends Controller{
    addTileLayer(url, options, sub){
        if (options == null || options == undefined){
            options = {
                someOpt:"opt"
            };
            console.log("qqqqqqq")
        } else {
            options = JSON.parse(options);
        }
        if (JSON.parse(sub).length != 0) {
            options.subdomains = JSON.parse(sub);
        }
        //options.subdomains = subD;
        const tileLayer = L.tileLayer(url, options);
        this.mapGroup.addLayer(tileLayer);
        const id = this.getLayerId(tileLayer);
        console.log("add tile to map "+url+" "+options+" id "+id);
        this.registerEvents(tileLayer);

        /*var nexrad = L.tileLayer.wms("http://mesonet.agron.iastate.edu/cgi-bin/wms/nexrad/n0r.cgi", {
            layers: 'nexrad-n0r-900913',
            format: 'image/png',
            transparent: true,
            attribution: "Weather data © 2012 IEM Nexrad"
        });*/

        this.mapGroup.addLayer(tileLayer);
        tileLayer.addTo(this.map);

        return id;
    }
    setUrl(id,url){
        this.getLayerById(id).setUrl(url);
    }
    getUrl(id){
        return this.getLayerById(id).getTileUrl();
    }

    registerEvents(layer){
        //const layer = this.getLayerById(id);
        console.log(this);
        const events = {
            tileload:(e)=>{
                //console.log("load");
                //console.log(e.tile.src);
                const event = new TileLayerEvent("tileload", layer._leaflet_id, e.tile.src, e.coords.x,e.coords.y, e.coords.z);
                event.eventClass = "TileEvent";
                eventController.fireEven(event);
            },
            remove: (e)=>{
                const event = new MapEvent('remove', layer._leaflet_id, L.latLng(0,0));
                event.eventClass = "LayerEvent";
                eventController.fireEven(event)
            }
        };
        layer.on(events);
    }
}

