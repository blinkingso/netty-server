package com.yz.chat.adapter;

import com.yz.chat.protocol.packet.CreateGroupRequestPacket;
import com.yz.chat.protocol.packet.CreateGroupResponsePacket;
import com.yz.chat.session.Group;
import com.yz.chat.session.Session;
import com.yz.chat.session.SessionUtil;
import com.yz.chat.utils.RandUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CreateGroupRequestAdapter extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket msg) throws Exception {
        CreateGroupResponsePacket response = new CreateGroupResponsePacket();
        Set<String> userIdSet = msg.getUserIdSet();
        response.setSuccess(false);
        response.setMessage("A group at least has two sessions included");
        if (userIdSet == null || userIdSet.isEmpty()) {
            ctx.channel().writeAndFlush(response);
            log.warn("Create group failed cause of no enough sessions");
            return;
        }

        final ChannelGroup channelGroup = new DefaultChannelGroup(msg.getGroupName(), ctx.executor());
        final Set<Session> sessions = new HashSet<>();
        String groupId = RandUtil.randString(8);
        for (String userId : userIdSet) {
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                Session session = SessionUtil.getSession(channel);
                channelGroup.add(channel);
                sessions.add(session);
            }
        }
        if (channelGroup.isEmpty()) {
            ctx.channel().writeAndFlush(response);
            log.warn("Must has two more sessions in one group, but has only one");
            return;
        }

        // bind group
        Channel createUser = SessionUtil.getChannel(msg.getCreateUserId());
        Session createSession = SessionUtil.getSession(createUser);
        channelGroup.add(createUser);
        sessions.add(createSession);
        SessionUtil.bindChannelGroup(createSession, new Group(groupId, msg.getGroupName()), channelGroup);

        // create response
        response.setSuccess(true);
        response.setGroupId(groupId);
        response.setGroupSessions(sessions);
        response.setCreateUserId(msg.getCreateUserId());
        response.setGroupName(msg.getGroupName());

        channelGroup.writeAndFlush(response);
        log.info("Group create success: id is : {}", response.getGroupId());
        log.info("Group created, members are: {}", Arrays.toString(sessions.toArray()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // unbind
        super.channelInactive(ctx);
    }
}
