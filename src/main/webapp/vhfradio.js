var upbtn = document.getElementById("radio:upbtn");
var downbtn = document.getElementById("radio:downbtn");
var distressbtn = document.getElementById("radio:distressbtn");
var ch16btn = document.getElementById("radio:ch16");

var channeloutput = document.getElementById("radio:channeloutput");
var channelstatus = document.getElementById("radio:channelstatus");
var channeltype = document.getElementById("radio:channeltype");

var channeloutputValue = null;
var channelstatusValue = null;
var channeltypeValue = null;

var dscmsg = null;
var intervalID = setInterval(timer, 1000);
var time = null;

upbtn.onclick = function () {
    if (document.getElementById("radio:outputboard") !== null) {
        console.log("next");
        var msg = {
            type: "channel",
            operation: "next"
        };
        websocket.send(JSON.stringify(msg));
    }
};

downbtn.onclick = function () {
    if (document.getElementById("radio:outputboard") !== null) {
        console.log("previous");
        var msg = {
            type: "channel",
            operation: "previous"
        };
        websocket.send(JSON.stringify(msg));
    }
};

ch16btn.onclick = function () {
    if (document.getElementById("radio:outputboard") !== null) {
        requestChannel(16);
    }
};

function recording() {
    console.log("recording");
    channelstatus.innerHTML = "TX";
    var msg = {
        type: "channel",
        operation: "recording"
    };
    websocket.send(JSON.stringify(msg));
}

function stopRecording() {
    console.log("stopRecording");
    channelstatus.innerHTML = "";
    var msg = {
        type: "channel",
        operation: "stopRecording"
    };
    websocket.send(JSON.stringify(msg));
}

function fillOutputboard(msg) {
    channeloutputValue = new Intl.NumberFormat('en-IN', {minimumIntegerDigits: 2}).format(msg.channelNumber);
    channelstatusValue = msg.channelStatus;
    channeltypeValue = msg.channelType;

    assignValues();
}

function assignValues() {
    document.getElementById("radio:channeloutput").innerHTML = channeloutputValue.toString();
    document.getElementById("radio:channeltype").innerHTML = channeltypeValue;
    document.getElementById("radio:channelstatus").innerHTML = channelstatusValue;
    document.getElementById("radio:time").innerHTML = time.toString();
}

function sendDistress(msg) {
    msg += " ON " + time.toString();
    console.log("sending distress");
    var dsc = {
        type: "dsc",
        operation: "distress",
        message: msg
    };
    websocket.send(JSON.stringify(dsc));

    console.log(msg);
}

function sendIndividualCall(targetMmsi, targetChannel) {
    console.log("sending individual");
    var dsc = {
        type: "dsc",
        operation: "individual",
        channelNumber: targetChannel.toString(),
        targetMmsi: targetMmsi.toString()
    };
    websocket.send(JSON.stringify(dsc));

    console.log(dsc);
}

function ableToComply() {
    requestChannel(dscmsg.channelNumber);
    dscmsg.operation = "individualAck";
    dscmsg.answer = "able";
    console.log(dscmsg);
    websocket.send(JSON.stringify(dscmsg));
}

function unableToComply() {
    assignValues();
    dscmsg.operation = "individualAck";
    dscmsg.answer = "unable";
    console.log(dscmsg);
    websocket.send(JSON.stringify(dscmsg));
}

function getDscMessageValues(msg) {
    dscmsg = msg;
}

function assignDscMsgValues() {
    document.getElementById("radio:dsc").innerHTML =
            "RCVD " + dscmsg.message
            + "\nFROM\n"
            + dscmsg.sender;
}

function assignDscAckMsgValues() {
    var text;

    if (dscmsg.operation === "acknowledgement") {
        text = "RCVD ACK FROM "
                + dscmsg.acknowledger
                + " ON "
                + dscmsg.message
                + " OF "
                + dscmsg.sender;
    } else if (dscmsg.operation === "individualAck") {
        if (dscmsg.answer === "able") {
            text = "RCVD ABLE TO COMPLY FROM "
                    + dscmsg.targetMmsi
                    + " ON CH "
                    + dscmsg.channelNumber;
            requestChannel(dscmsg.channelNumber);
        } else if (dscmsg.answer === "unable") {
            text = "RCVD UNABLE TO COMPLY FROM "
                    + dscmsg.targetMmsi
                    + " ON CH "
                    + dscmsg.channelNumber;
        }
    }
    document.getElementById("radio:dscack").innerHTML = text;
}

function assignDscIndCallValues() {
    document.getElementById("radio:dscindcall").innerHTML =
            "RCVD INDIVIDUAL CALL FROM "
            + dscmsg.senderMmsi
            + " CHANNEL "
            + dscmsg.channelNumber;
}

function sendAcknowledgement() {
    dscmsg.operation = "acknowledgement";
    websocket.send(JSON.stringify(dscmsg));
    console.log(dscmsg);
}

function requestChannel(chNumber) {
    console.log("request " + chNumber);
    var request = {
        type: "channel",
        operation: "requestChannel",
        channelNumber: chNumber
    };
    websocket.send(JSON.stringify(request));

    console.log(request);
}

function requestMmsi() {
    console.log("request MMSI");
    var request = {
        type: "mmsi"
    };
    websocket.send(JSON.stringify(request));

    console.log(request);
}

function fillMmsi(mmsi) {
    document.getElementById("radio:mmsi").innerHTML = mmsi;
}

function timer() {
    var d = new Date();
    time = new Intl.NumberFormat('en-IN', {minimumIntegerDigits: 2}).format(d.getUTCHours().toLocaleString())
            + ":"
            + new Intl.NumberFormat('en-IN', {minimumIntegerDigits: 2}).format(d.getUTCMinutes().toLocaleString())
            + " UTC";

    if (document.getElementById("radio:outputboard") !== null) {
        document.getElementById("radio:time").innerHTML = time.toString();
    }
}