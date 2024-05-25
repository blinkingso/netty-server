package com.yz.chat.encoder;

import com.yz.chat.protocol.Packet;
import com.yz.chat.protocol.ProtocolCodeC;
import com.yz.chat.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        ProtocolCodeC.INSTANCE.encode(out, Serializer.DEFAULT, msg);
    }
}
