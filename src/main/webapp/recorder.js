/* global channelstatus */

if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    console.log('getUserMedia supported.');
} else {
    console.log('getUserMedia not supported on your browser!');
    alert("Notification.\nAudio Recording capabilities can not be served in this browser.");
}

var pttbtn = document.getElementById("radio:pttbtn");

var constraints = {audio: true};
var mediaRecorder;

var success = function (stream) {
    console.log('Recorder - Success');
    mediaRecorder = new MediaRecorder(stream);

    pttbtn.onclick = function () {
        if (mediaRecorder.state === 'recording') {
            javaInvoker();
            mediaRecorder.stop();
            stopRecording();
            pttbtn.style.background = "";
            pttbtn.style.color = "";
        } else if (mediaRecorder.state === 'inactive') {
            javaInvoker();
            mediaRecorder.start();
            recording();
            pttbtn.style.background = "red";
            pttbtn.style.color = "black";
        }
    };

    var chunks = [];
    mediaRecorder.ondataavailable = function (e) {
        chunks.push(e.data);
    };

    mediaRecorder.onstart = function (e) {
        console.log("MediaRecorder: start");
    };

    mediaRecorder.onstop = function (e) {
        console.log("MediaRecorder: stop");
        var blob = new Blob(chunks, {'type': 'audio/ogg; codecs=opus'});
        chunks = [];
        websocket.send(blob);
    };
};

var error = function (err) {
    console.log('The following getUserMedia error occured: ' + err);
};

function playAudio(blob) {
    console.log(blob);

    var blobURL = window.URL.createObjectURL(blob);
    var audio = new Audio(blobURL);
    audio.play();

    audio.addEventListener('ended', function () {
        console.log("playback finished");
        channelstatus.innerHTML = "";
    }, false);

    audio.addEventListener('play', function () {
        console.log("playback play");
        channelstatus.innerHTML = "BUSY";
    }, false);
}

// Main Recording function
navigator.mediaDevices.getUserMedia(constraints).then(success).catch(error);


javaInvoker = function () {
    if (mediaRecorder.state === 'recording') {
        recorderStopped();
    } else if (mediaRecorder.state === 'inactive') {
        recorderStarted();
    }
};