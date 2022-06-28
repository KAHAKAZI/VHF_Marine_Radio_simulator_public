package org.vhfradio.model;

import java.util.HashSet;
import java.util.Set;
import javax.websocket.Session;
import org.primefaces.json.JSONObject;

public class Channel {
    
    private final int channelIndex;
    
    private final int channelNumber;
    
    private ChannelStatus channelStatus;
    
    private final ChannelType channelType;
    
    private final Set<Session> channelUsers;

    public Channel(int index, int number, ChannelStatus status, ChannelType type) {
        this.channelIndex = index;
        this.channelNumber = number;
        this.channelStatus = status;
        this.channelType = type;
        this.channelUsers = new HashSet<>();
    }

    public int getIndex(){
        return this.channelIndex;
    }
    
    public int getNumber() {
        return channelNumber;
    }

    public String getStatus() {
        return channelStatus.toString();
    }

    public void setStatus(ChannelStatus status) {
        this.channelStatus = status;
    }

    public String getType() {
        return channelType.toString();
    }
    
    public void addSession(Session session){
        this.channelUsers.add(session);
    }
    
    public void removeSession(Session session){
        this.channelUsers.remove(session);
    }
    
    public Set<Session> getSessions(){
        return channelUsers;
    }
    
    public JSONObject getChannelAsJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "channel");
        jsonObject.put("channelNumber", this.getNumber());
        jsonObject.put("channelStatus", this.getStatus());
        jsonObject.put("channelType", this.getType());
        return jsonObject;
    }

}
