package com.yz.chat.server;

import com.yz.chat.handler.HandlerFactory;
import com.yz.chat.protocol.Packet;
import com.yz.chat.protocol.ProtocolCodeC;
import com.yz.chat.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        Packet packet = ProtocolCodeC.INSTANCE.decode(buffer);
        try {
            HandlerFactory.getHandler(packet.getClass()).handle(packet, Serializer.DEFAULT, ctx.channel());
        } catch (Exception e) {
            log.error("Packet handler error for: {}", packet.getClass().getSimpleName(), e);
        }
    }

}
