package com.yz.chat.handler;

import com.yz.chat.protocol.Packet;
import com.yz.chat.protocol.ProtocolCodeC;
import com.yz.chat.protocol.packet.MessageRequestPacket;
import com.yz.chat.protocol.packet.MessageResponsePacket;
import com.yz.chat.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageRequestHandler implements PacketHandler {

    @Override
    public void handle(Packet packet, Serializer serializer, Channel channel) {
        MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
        log.info("Received message request from {}, mesasge is: {}", channel.remoteAddress(), messageRequestPacket.getMessage());
        MessageResponsePacket resp = new MessageResponsePacket();
        resp.setMessage("Server response -> " + messageRequestPacket.getMessage());
        ByteBuf responseBuf = ProtocolCodeC.INSTANCE.encode(channel.alloc(), serializer, resp);
        channel.writeAndFlush(responseBuf);
    }
}
