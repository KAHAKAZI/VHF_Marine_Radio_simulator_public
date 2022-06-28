package org.vhfradio.model;

import java.util.Arrays;
import java.util.List;

public class Channels {

    private final List<Channel> channels = Arrays.asList(
            new Channel(0, 1, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(1, 2, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(2, 3, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(3, 4, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(4, 5, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(5, 6, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(6, 7, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(7, 8, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(8, 9, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(9, 10, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(10, 11, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(11, 12, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(12, 13, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(13, 14, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(14, 15, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(15, 16, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(16, 17, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(17, 18, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(18, 19, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(19, 20, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(20, 21, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(21, 22, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(22, 23, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(23, 24, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(24, 25, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(25, 26, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(26, 27, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(27, 28, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(28, 60, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(29, 61, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(30, 62, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(31, 63, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(32, 64, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(33, 65, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(34, 66, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(35, 67, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(36, 68, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(37, 69, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(38, 71, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(39, 72, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(40, 73, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(41, 74, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(42, 75, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(43, 76, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(44, 77, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(45, 78, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(46, 79, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(47, 80, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(48, 81, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(49, 82, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(50, 83, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(51, 84, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(52, 85, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(53, 86, ChannelStatus.EMPTY, ChannelType.DUP),
            new Channel(54, 87, ChannelStatus.EMPTY, ChannelType.SIM),
            new Channel(55, 88, ChannelStatus.EMPTY, ChannelType.SIM)
    );

    private final int firstIndex = 0;
    private final int lastIndex = channels.size() - 1;

    public List<Channel> getChannels() {
        return channels;
    }

    public Channel getByNumber(int number) {
        Channel ch = null;

        for (Channel channel : channels) {
            if (channel.getNumber() == number) {
                ch = channel;
                break;
            }
        }
        return ch;
    }

    public Channel getByIndex(int channelIndex) {
        Channel ch = null;

        for (Channel channel : channels) {
            if (channel.getIndex() == channelIndex) {
                ch = channel;
                break;
            }
        }
        return ch;
    }

    public Channel getNext(Channel currentChannel) {
        int currentChannelIndex = currentChannel.getIndex();

        if (currentChannelIndex == lastIndex) {
            return getByIndex(firstIndex);
        } else {
            return getByIndex(++currentChannelIndex);
        }
    }

    public Channel getPrevious(Channel currentChannel) {
        int currentChannelIndex = currentChannel.getIndex();

        if (currentChannelIndex == firstIndex) {
            return getByIndex(lastIndex);
        } else {
            return getByIndex(--currentChannelIndex);
        }
    }

}
