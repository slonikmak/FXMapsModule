  class MarkerController extends Controller{

    addMarker(lat, lng, options, iconOptions){
        let optionsObj = JSON.parse(options);
        const marker = L.marker([lat, lng]);
        this.mapGroup.addLayer(marker);
        marker.addTo(this.map);
        const id = this.getLayerId(marker);
        if (iconOptions != null && iconOptions !== undefined && iconOptions !== "null"){
            this.setIcon(id, iconOptions);
        }
        this.registerEvents(marker);
        return id
    }

    startMarker(){
        let marker = this.map.editTools.startMarker();
        console.log(marker);
        this.mapGroup.addLayer(marker);
        const id = this.getLayerId(marker);
        this.registerEvents(id);
        marker.on("click", function () {

        })
        return id
    }

    on(id, eventType, f){
        this.getLayerById(id).on(eventType, f);
    }

    toGeoJson(id){
        return JSON.stringify(this.getLayerById(id).toGeoJSON());
    }

    getLangLng(id){
        return this.getLayerById(id).getLatLng()
    }

    setLatLng(id, latLng){
        this.getLayerById(id).setLatLng(latLng)
    }

    setIcon(id, options){
        //console.log(JSON.parse(properties));
        const icon = L.icon(JSON.parse(options));
        this.getLayerById(id).setIcon(icon)
    }

    setOpacity(id,opacity){
        this.getLayerById(id).setOpacity(opacity)
    }

    update(id, lat, lng,  options){
        console.log("update marker "+id+" "+lat);

        if (id === 0) return;
        options = JSON.parse(options);
        //this.setIcon(id, properties);
        this.setLatLng(id, [lat, lng]);
        this.setOpacity(id, options.opacity);
    }

    hide(id){
        this.map.findLayer(id).setOpacity(0);
    }

    registerEvents(layer){
        //const layer = this.map.findLayer(id);
        const id = layer._leaflet_id;

        //const marker = this.getLayerById(id);

        const events = {
            click: (e)=>{
                //console.log(e);
                const event = new MapEvent('click', id, e.latlng);
                event.eventClass = "MouseEvent";
                eventController.fireEven(event)
            },
            move: (e)=>{
                //console.log("move")
                const event = new MapEvent('move', id, e.latlng);
                event.eventClass = "LayerEvent";
                eventController.fireEven(event)
            },
            mouseover: (e)=>{
                const event = new MapEvent('mouseover', id, e.latlng);
                event.eventClass = "MouseEvent";
                eventController.fireEven(event)
            },
            mouseout: (e)=>{
                const event = new MapEvent('mouseout', id, e.latlng);
                event.eventClass = "MouseEvent";
                eventController.fireEven(event)
            },
            add: (e)=>{
                const event = new MapEvent('add', id, e.latlng);
                event.eventClass = "LayerEvent";
                eventController.fireEven(event)
            },
            remove: (e)=>{
                //console.log("remove marker");
                const event = new MapEvent('remove', id, L.latLng(0,0));
                event.eventClass = "LayerEvent";
                eventController.fireEven(event)
                //console.log(event);
            }
        };

        layer.on(events)
    }
}

