package com.yz.chat.adapter;

import com.yz.chat.protocol.MessageType;
import com.yz.chat.protocol.packet.MessageRequestPacket;
import com.yz.chat.protocol.packet.MessageResponsePacket;
import com.yz.chat.session.Session;
import com.yz.chat.session.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageRequestChannelAdapter extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
        MessageType mType = msg.getMType();
        switch (mType) {
            case User -> processUserMessage(ctx, msg);
            case Group -> processGroupMessage(ctx, msg);
            default -> processUnknownMessageType(ctx, msg);
        }
    }

    private void processUserMessage(ChannelHandlerContext ctx, MessageRequestPacket msg) {
        Session currentSession = SessionUtil.getSession(ctx.channel());
        log.info("Received MessageRequestPacket: fromUser: {}, toUser: {}, with: {}", currentSession.getUserId(), msg.getToUserId(), msg.getMessage());
        Channel toChannel = SessionUtil.getChannel(msg.getToUserId());
        if (toChannel != null) {
            MessageResponsePacket response = new MessageResponsePacket();
            response.setVersion(msg.getVersion());
            response.setFromUserId(currentSession.getUserId());
            response.setFromUserName(currentSession.getUserName());
            response.setMessage(msg.getMessage());
            toChannel.writeAndFlush(response);
        }
    }

    private void processGroupMessage(ChannelHandlerContext ctx, MessageRequestPacket msg) {
        Session session = SessionUtil.getSession(ctx.channel());
        String groupId = msg.getToUserId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        MessageResponsePacket response = new MessageResponsePacket();
        response.setFromUserId(session.getUserId());
        response.setFromUserName(session.getUserName());
        response.setMessage(msg.getMessage());
        if (channelGroup != null && !channelGroup.isEmpty()) {
            for (Channel channel : channelGroup) {
                Session s = SessionUtil.getSession(channel);
                if (!s.getUserId().equals(session.getUserId())) {
                    channel.writeAndFlush(response);
                }
            }
        }
    }

    private void processUnknownMessageType(ChannelHandlerContext ctx, MessageRequestPacket msg) {
        log.warn("Received an unknown message type: {}", msg.getMessage());
    }
}
