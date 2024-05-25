package com.yz.chat.adapter;

import com.yz.chat.protocol.packet.JoinGroupRequestPacket;
import com.yz.chat.session.Session;
import com.yz.chat.session.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinGroupRequestAdapter extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket msg) throws Exception {
        log.info("Received a `join group` packet: {}", msg);
        Session session = SessionUtil.getSession(ctx.channel());
        String groupId = msg.getGroupId();
    }
}
