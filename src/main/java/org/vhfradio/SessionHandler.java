package org.vhfradio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import org.vhfradio.model.Channel;
import org.vhfradio.model.ChannelStatus;
import org.vhfradio.model.Channels;
import org.vhfradio.model.MMSI;

@ApplicationScoped
public class SessionHandler {

    private static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());
    
    private List<String> mmsiNumbers = new ArrayList<String>();

    private Channels channels = new Channels();
    
    private String recorderSession = null;

    public Set<Session> getUsers() {
        return users;
    }

    public Channels getChannelsHandler() {
        return channels;
    }

    public List<String> getMmsiNumbers() {
        return mmsiNumbers;
    }

    public void addUser(Session user) throws IOException {
        user.getUserProperties().put(Properties.CHANNEL, channels.getByNumber(16));
        channels.getByNumber(16).addSession(user);
        user.getUserProperties().put(Properties.MMSI, new MMSI(mmsiNumbers).getMmsi());
        mmsiNumbers.add(user.getUserProperties().get(Properties.MMSI).toString());
        users.add(user);
    }

    public void removeUser(Session user) {
        Channel currentChannel = (Channel) user.getUserProperties().get(Properties.CHANNEL);
        String mmsi = user.getUserProperties().get(Properties.MMSI).toString();
        currentChannel.removeSession(user);
        mmsiNumbers.remove(mmsi);
        users.remove(user);
    }

    public String getRecorderSession() {
        return recorderSession;
    }

    public void setRecorderSession(String recorderSession) {
        this.recorderSession = recorderSession;
    }

    public Channel getChannel(Session session){
        return (Channel) session.getUserProperties().get(Properties.CHANNEL);
    }
    
    void requestChannel(Session session, int channelNumber) {
        Channel currentCh = (Channel) session.getUserProperties().get(Properties.CHANNEL);
        Channel nextCh = channels.getByNumber(channelNumber);
        session.getUserProperties().replace(Properties.CHANNEL, nextCh);
        currentCh.removeSession(session);
        nextCh.addSession(session);
    }

    public void nextChannel(Session session) {
        Channel currentCh = (Channel) session.getUserProperties().get(Properties.CHANNEL);
        Channel nextCh = channels.getNext(currentCh);
        session.getUserProperties().replace(Properties.CHANNEL, nextCh);
        currentCh.removeSession(session);
        nextCh.addSession(session);
    }

    public void previousChannel(Session session) {
        Channel currentCh = (Channel) session.getUserProperties().get(Properties.CHANNEL);
        Channel previousCh = channels.getPrevious(currentCh);
        session.getUserProperties().replace(Properties.CHANNEL, previousCh);
        currentCh.removeSession(session);
        previousCh.addSession(session);
    }
    
    public void busyChannel(Session session) {        
        Channel channel = (Channel) session.getUserProperties().get(Properties.CHANNEL);
        session.getUserProperties().put(Properties.RECORDER, "true");
        recorderSession = session.getId();
        channel.setStatus(ChannelStatus.BUSY);
    }
    
    public void freeChannel(Session session) {
        Channel channel = (Channel) session.getUserProperties().get(Properties.CHANNEL);
        session.getUserProperties().remove(Properties.RECORDER);
        recorderSession = null;
        channel.setStatus(ChannelStatus.EMPTY);
    }

}
