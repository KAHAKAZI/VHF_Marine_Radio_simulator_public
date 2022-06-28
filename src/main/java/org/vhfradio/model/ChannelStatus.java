package org.vhfradio.model;

public enum ChannelStatus {
    BUSY("BUSY"),
    TX("TX"),
    EMPTY("");

    private final String channelStatus;

    ChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
    }

    @Override
    public String toString() {
        return this.channelStatus;
    }

}
