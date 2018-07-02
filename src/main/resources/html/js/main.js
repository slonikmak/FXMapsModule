
const map = L.map('map',{editable: true, preferCanvas: true}).setView([51.505, -0.09], 13);

L.Map.include({
    findLayer: function (id) {
        for (let i in this._layers) {
            if (this._layers[i]._leaflet_id === id) {
                return this._layers[i];
            }
        }
    }
});

const mapGroup = L.layerGroup();
//mapGroup.addTo(map);

/*L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);*/

const tileLayerController = new TileLayerController(map, mapGroup);
const markerController = new MarkerController(map, mapGroup);
const editableController = new EditableController(map, mapGroup);
const polyLineController = new PolylineController(map, mapGroup);
const circleController = new CircleController(map, mapGroup);
const polygonController = new PolygonController(map, mapGroup);
const missionController = new MissionController(map, mapGroup);
map.editTools = new L.MyEditable(map, {});



///Register map events
//TODO: remove register to map controller
map.on('click', (e)=>{
    const event = new MapEvent(e.type, e.target._leaflet_id,L.latLng(e.latlng.lat, e.latlng.lng), null, "MouseEvent");
    //console.log(event);
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

