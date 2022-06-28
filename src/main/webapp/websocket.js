var wsUri = "ws://" + document.location.host + document.location.pathname + "vhfradio";
var websocket = new WebSocket(wsUri);

websocket.onopen = function() {
    console.log("Websocket: opened");
};

websocket.onclose = function() {
    console.log("Websocket: closed");
};

websocket.onerror = function(evt) {
    console.log("Error: " + evt.data);
};

websocket.onmessage = function(evt) {
    console.log("Message: " + evt.data);
    
    if (typeof evt.data === "string") {
        var msg = JSON.parse(evt.data);

        switch (msg.type) {
            case "channel":
                fillOutputboard(msg);
                break;
            case "dsc":
                console.log(msg);
                if (msg.operation === "distress") {
                    showDscMessageForm();
                    getDscMessageValues(msg);
                } else if (msg.operation === "acknowledgement") {
                    showAckMessageForm();
                    getDscMessageValues(msg);
                } else if (msg.operation === "individual") {
                    showDscIndCallForm();
                    getDscMessageValues(msg);
                } else if(msg.operation === "individualAck") {
                    showAckMessageForm();
                    getDscMessageValues(msg);
                }
                break;
            case "mmsi":
                console.log(msg);
                fillMmsi(msg.mmsiNumber);
                break;
        }
    } else {
        playAudio(evt.data);
    }
};