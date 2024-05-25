package com.yz.chat.handler;

import com.yz.chat.protocol.Packet;
import com.yz.chat.protocol.packet.MessageResponsePacket;
import com.yz.chat.serialize.Serializer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageResponseHandler implements PacketHandler {

    @Override
    public void handle(Packet packet, Serializer serializer, Channel channel) {
        MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
        log.info(messageResponsePacket.getMessage());
    }
}
