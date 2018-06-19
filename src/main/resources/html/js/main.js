const map = L.map('map',{editable: true}).setView([51.505, -0.09], 13);

/*L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);*/

const tileLayerController = new TileLayerController(map);
const markerController = new MarkerController(map);
const editableController = new EditableController(map);
const polyLineController = new PolylineController(map);



///Register map events
//TODO: remove register to map controller
map.on('click', (e)=>{
    const event = new MapEvent(e.type, e.target._leaflet_id,e.latlng, null, "MouseEvent");
    //markerController.addMarker(e.latlng.lat, e.latlng.lng,{});
    mapEventController.fireEventFromJS(JSON.stringify(event));
});


///Get map ID
///TODO: remove to map controller
function getMapId() {
    return map._leaflet_id;
}

