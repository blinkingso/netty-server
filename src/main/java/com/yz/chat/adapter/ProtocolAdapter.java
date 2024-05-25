package com.yz.chat.adapter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import static com.yz.chat.protocol.ProtocolCodeC.MAGIC_NUMBER;

@Slf4j
public class ProtocolAdapter extends LengthFieldBasedFrameDecoder {

    public ProtocolAdapter() {
        super(Integer.MAX_VALUE, 7, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.getInt(in.readerIndex()) != MAGIC_NUMBER) {
            log.warn("Unexpected magic number: {}, channel closed.", in.getInt(in.readerIndex()));
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
