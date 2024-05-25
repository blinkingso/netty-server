package com.yz.chat.adapter;

import com.yz.chat.protocol.packet.CreateGroupResponsePacket;
import com.yz.chat.session.Group;
import com.yz.chat.session.Session;
import com.yz.chat.session.SessionUtil;
import com.yz.chat.support.Attributes;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CreateGroupResponseAdapter extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket msg) throws Exception {
        Session session = SessionUtil.getSession(ctx.channel());
        if (msg.isSuccess()) {
            Set<Session> sessions = msg.getGroupSessions();
            Group group = new Group(msg.getGroupId(), msg.getGroupName());
            bindGroup(ctx.channel(), group);
            log.info("Group create successfully, users are: ");
            for (Session session1 : sessions) {
                log.info("\t{} -> {}", session1.getUserId(), session1.getUserName());
            }
        } else {
            if (session.getUserId().equals(msg.getCreateUserId())) {
                log.warn("Failed to create a group for: {}", msg.getMessage());
            }
        }
    }

    private void bindGroup(Channel channel, Group group) {
        Set<Group> groups = channel.attr(Attributes.GROUP).get();
        if (groups == null) {
            groups = new HashSet<>();
        }
        groups.add(group);
        channel.attr(Attributes.GROUP).set(groups);
    }
}
