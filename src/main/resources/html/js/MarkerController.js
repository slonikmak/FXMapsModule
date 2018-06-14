class MarkerController extends Controller{

    addMarker(lat, lng, options){
        const marker = L.marker([lat, lng], options);
        this.mainGroup.addLayer(marker);
        const id = this.getLayerId(marker);
        this.registerEvents(id);
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

    setIcon(id, icon){
        this.getLayerById(id).setIcon(L.icon({iconUrl:icon}))
    }

    setOpacity(id,opacity){
        this.getLayerById(id).setOpacity(opacity)
    }

    registerEvents(id){
        const marker = this.getLayerById(id);

        const events = {
            click: (e)=>{
                const event = new MapEvent('click', id);
                event.eventClass = "Event";
                eventController.fireEven(event)
            },
            move: (e)=>{
                const event = new MapEvent('move', id);
                event.eventClass = "Event";
                eventController.fireEven(event)
            },
            mouseover: (e)=>{
                const event = new MapEvent('mouseover', id);
                event.eventClass = "MouseEvent";
                eventController.fireEven(event)
            },
            mouseout: (e)=>{
                const event = new MapEvent('mouseout', id);
                event.eventClass = "MouseEvent";
                eventController.fireEven(event)
            },
            add: (e)=>{
                const event = new MapEvent('add', id);
                event.eventClass = "Event";
                eventController.fireEven(event)
            },
            remove: (e)=>{
                const event = new MapEvent('remove', id);
                event.eventClass = "Event";
                eventController.fireEven(event)
            }
        };

        marker.on(events)
    }
}

