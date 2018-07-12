class GeoJsonController extends Controller{
    addLayer(json){
        L.geoJSON(JSON.parse(json), {
            style: function (feature) {
                return {color: "green"};
            }
        }).bindPopup(function (layer) {
            return "GeoJsonLayer"
            //return layer.feature.properties.description;
        }).addTo(this.map);
    }

}