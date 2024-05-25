package com.yz.chat.handler;

import com.yz.chat.protocol.Packet;
import com.yz.chat.serialize.Serializer;
import io.netty.channel.Channel;

@FunctionalInterface
public interface PacketHandler {

    void handle(Packet packet, Serializer serializer, Channel channel);
}
