  class MarkerController extends Controller{

    addMarker(lat, lng, options, iconSrc){

        console.log(iconSrc);
        var myIcon = L.icon({
            iconUrl: iconSrc,
            iconSize: [48, 48],
        });
        let optionsObj = JSON.parse(options);
        if (iconSrc != null){
            optionsObj.icon = myIcon;
        }
        console.log(lat+" "+lng+" "+optionsObj);
        const marker = L.marker([lat, lng], optionsObj);
        this.mainGroup.addLayer(marker);
        const id = this.getLayerId(marker);
        this.registerEvents(id);
        return id
    }

    startMarker(){
        let marker = this.map.editTools.startMarker();
        console.log(marker);
        this.mainGroup.addLayer(marker);
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
                console.log(e);
                const event = new MapEvent('click', id, e.latlng);
                event.eventClass = "MouseEvent";
                eventController.fireEven(event)
            },
            move: (e)=>{
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

