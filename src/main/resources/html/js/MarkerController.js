  class MarkerController extends Controller{

    addMarker(lat, lng, options, iconOptions){
        let optionsObj = JSON.parse(options);
        const marker = L.marker([lat, lng]);
        this.mapGroup.addLayer(marker);
        const id = this.getLayerId(marker);
        if (iconOptions != null && iconOptions !== undefined && iconOptions !== "null"){
            this.setIcon(id, iconOptions);
        }
        this.registerEvents(id);
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
        return this.getLayerById(id).toGeoJSON()
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

    update(id, options){
        options = JSON.parse(options);
        console.log(options);
        //this.setIcon(id, properties);
        this.setOpacity(id, options.opacity);
    }

    registerEvents(id){
        const marker = this.getLayerById(id);

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
                const event = new MapEvent('add', id);
                event.eventClass = "LayerEvent";
                eventController.fireEven(event)
            },
            remove: (e)=>{
                const event = new MapEvent('remove', id);
                event.eventClass = "LayerEvent";
                eventController.fireEven(event)
            }
        };

        marker.on(events)
    }
}

