const map = L.map('map').setView([51.505, -0.09], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

const markerController = new MarkerController(map);


map.on('click', (e)=>{
    const event = new MapEvent(e.type, e.target._leaflet_id,e.latlng);
    //markerController.addMarker(e.latlng.lat, e.latlng.lng,{});
    eventController.fireEven(event);
});