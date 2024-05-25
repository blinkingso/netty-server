package com.yz.chat.adapter;

import com.yz.chat.utils.StatisticDataUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class StatisticDataAdapter extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        StatisticDataUtil.activeChannel();
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int currentBuf = byteBuf.readableBytes();
        StatisticDataUtil.flow(currentBuf);
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        StatisticDataUtil.inactiveChannel();
        super.channelInactive(ctx);
    }
}
