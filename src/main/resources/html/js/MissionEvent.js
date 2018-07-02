class MissionEvent {

    constructor(type,target,latlng, eventClass, layer){
        this.type = type;
        this.target = target;
        this.latlng = latlng;
        this.eventClass = eventClass;
        this.layer = layer;
    }
}