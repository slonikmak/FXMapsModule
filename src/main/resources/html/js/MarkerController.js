  class MarkerController extends Controller{

    addMarker(lat, lng, options){

        var myIcon = L.icon({
            iconUrl: 'file:/C:/Users/sloni/Downloads/icons8-48.png',
            iconSize: [48, 48],
        });
        let optionsObj = JSON.parse(options);
        //optionsObj.icon = myIcon;
        console.log(lat+" "+lng+" "+optionsObj);
        const marker = L.marker([lat, lng], optionsObj);
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
                const event = new MapEvent('click', id, e.latlng);
                event.eventClass = "Event";
                eventController.fireEven(event)
            },
            move: (e)=>{
                const event = new MapEvent('move', id, e.latlng);
                event.eventClass = "Event";
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

