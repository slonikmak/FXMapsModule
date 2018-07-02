const eventController = {};
/*let javaEventController = {
    fireEvent: function (event) {
        console.log("jjj");
    }
};*/

eventController.eventListeners = new Map();

eventController.addEventListner = function(eventType, listner){
    if (!this.eventListeners.has(eventType)) {
        //console.log("add listner "+eventType)
        this.eventListeners.set(eventType, [])
    }
    this.eventListeners.get(eventType).push(listner)
};

eventController.fireEven = function(event){
    //console.log("fire event");
    //console.log(event);
    javaEventController.fireEvent(JSON.stringify(event));
    const listeners = this.eventListeners.get(event.type);
    if (listeners !== undefined){
        for (let i = 0;i<listeners.length; i++){
            listeners[i](event);
        }
    }
};