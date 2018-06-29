
const map = L.map('map',{editable: true, preferCanvas: true}).setView([51.505, -0.09], 13);
const mapGroup = L.layerGroup();
mapGroup.addTo(map);

/*L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);*/

const tileLayerController = new TileLayerController(map, mapGroup);
const markerController = new MarkerController(map, mapGroup);
const editableController = new EditableController(map, mapGroup);
const polyLineController = new PolylineController(map, mapGroup);
const circleController = new CircleController(map, mapGroup);
const polygonController = new PolygonController(map, mapGroup);



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

function flyTo(lat, lng) {
    map.flyTo([lat,lng], 18);
}

