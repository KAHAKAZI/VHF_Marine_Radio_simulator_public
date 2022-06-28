package org.vhfradio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.primefaces.json.JSONObject;

@ServerEndpoint(value = "/vhfradio")
public class WebsocketServer {

    @Inject
    SessionHandler sessionHandler;

    @OnOpen
    public void onOpen(Session session) {
        try {
            sessionHandler.addUser(session);
            System.out.println("Session with MMSI " + session.getUserProperties().get(Properties.MMSI) + " added");
            System.out.println("Users on channel: " + sessionHandler.getChannelsHandler().getByNumber(16).getSessions().size());
            System.out.println("MMSIs: " + sessionHandler.getMmsiNumbers().size());
            System.out.println(sessionHandler.getChannel(session).getChannelAsJson().toString());
            session.getBasicRemote().sendText(sessionHandler.getChannel(session).getChannelAsJson().toString());
        } catch (IOException ex) {
            Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public void onTextMessage(Session session, String message) {
        try {
            System.out.println("Text message: " + message);
            JSONObject json = new JSONObject(message);

            switch (json.getString("type")) {
                case "channel":
                    System.out.println("channel message");
                    switch (json.getString("operation")) {
                        case "next":
                            System.out.println(json.getString("operation"));
                            sessionHandler.nextChannel(session);
                            session.getBasicRemote().sendText(sessionHandler.getChannel(session).getChannelAsJson().toString());
                            break;
                        case "previous":
                            System.out.println(json.getString("operation"));
                            sessionHandler.previousChannel(session);
                            session.getBasicRemote().sendText(sessionHandler.getChannel(session).getChannelAsJson().toString());
                            break;
                        case "requestChannel":
                            System.out.println(json.getString("operation"));
                            sessionHandler.requestChannel(session, json.getInt("channelNumber"));
                            session.getBasicRemote().sendText(sessionHandler.getChannel(session).getChannelAsJson().toString());
                            break;
                        case "recording":
                            System.out.println(json.getString("operation"));
                            sessionHandler.busyChannel(session);
                            for (Session user : sessionHandler.getChannel(session).getSessions()) {
                                if (!session.equals(user)) {
                                    user.getBasicRemote().sendText(sessionHandler.getChannel(session).getChannelAsJson().toString());
                                }
                            }
                            break;
                        case "stopRecording":
                            System.out.println(json.getString("operation"));
                            sessionHandler.freeChannel(session);
                            for (Session user : sessionHandler.getChannel(session).getSessions()) {
                                if (!session.equals(user)) {
                                    user.getBasicRemote().sendText(sessionHandler.getChannel(session).getChannelAsJson().toString());
                                }
                            }
                            break;
                    }
                    break;
                case "dsc":
                    switch (json.getString("operation")) {
                        case "distress":
                            json.put("sender", session.getUserProperties().get(Properties.MMSI).toString());
                            System.out.println("DSC msg: " + json.toString());
                            for (Session user : sessionHandler.getUsers()) {
                                if (!session.equals(user)) {
                                    user.getBasicRemote().sendText(json.toString());
                                }
                            }
                            break;
                        case "acknowledgement":
                            json.put("acknowledger", session.getUserProperties().get(Properties.MMSI).toString());
                            System.out.println("ACK msg: " + json.toString());
                            for (Session user : sessionHandler.getUsers()) {
                                if (!session.equals(user)) {
                                    user.getBasicRemote().sendText(json.toString());
                                }
                            }
                            break;

                        case "individual":
                            json.put("senderMmsi", session.getUserProperties().get(Properties.MMSI).toString());
                            System.out.println("Ind msg: " + json.toString());
                            for (Session user : sessionHandler.getUsers()) {
                                if (json.getString("targetMmsi").equals((String) user.getUserProperties().get(Properties.MMSI))) {
                                    user.getBasicRemote().sendText(json.toString());
                                    System.out.println("individual sent");
                                }
                            }
                            break;
                        case "individualAck":
                            System.out.println("Ind msg: " + json.toString());
                            for (Session user : sessionHandler.getUsers()) {
                                if (json.getString("senderMmsi").equals((String) user.getUserProperties().get(Properties.MMSI))) {
                                    user.getBasicRemote().sendText(json.toString());
                                    System.out.println("individual sent");
                                }
                            }
                            break;
                    }
                    System.out.println("dsc message");
                    break;
                case "mmsi":
                    json.put("mmsiNumber", session.getUserProperties().get(Properties.MMSI).toString());
                    session.getBasicRemote().sendText(json.toString());
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, message, ex);
        }
    }

    @OnMessage
    public void onBinaryMessage(Session session, ByteBuffer message) {
        System.out.println("Binary message: " + message.toString());
        try {
            for (Session user : sessionHandler.getChannel(session).getSessions()) {
                if (!user.equals(session)) {
                    user.getBasicRemote().sendBinary(message);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, "Recording", ex);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("User: " + session.getUserProperties().toString());

        try {
            boolean ifRecorder = session.getUserProperties().containsKey(Properties.RECORDER);
            System.out.println("Closing: " + ifRecorder);
            if (ifRecorder) {
                sessionHandler.freeChannel(session);
                for (Session user : sessionHandler.getChannel(session).getSessions()) {
                    user.getBasicRemote().sendText(sessionHandler.getChannel(session).getChannelAsJson().toString());
                }
            }
            sessionHandler.removeUser(session);
            System.out.println("Session removed. " + reason.toString());
            System.out.println("Users on channel: " + sessionHandler.getChannelsHandler().getByNumber(16).getSessions().size());
            System.out.println("MMSIs: " + sessionHandler.getMmsiNumbers().size());
        } catch (IOException ex) {
            Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.err.println("Error" + t.getMessage());
    }
}
