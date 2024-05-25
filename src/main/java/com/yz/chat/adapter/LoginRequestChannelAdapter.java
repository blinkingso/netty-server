package com.yz.chat.adapter;

import com.yz.chat.protocol.packet.LoginRequestPacket;
import com.yz.chat.protocol.packet.LoginResponsePacket;
import com.yz.chat.session.Session;
import com.yz.chat.session.SessionUtil;
import com.yz.chat.utils.LoginUtil;
import com.yz.chat.utils.RandUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginRequestChannelAdapter extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
        ctx.channel().writeAndFlush(login(ctx.channel(), msg));
    }

    private LoginResponsePacket login(Channel channel, LoginRequestPacket loginRequestPacket) {
        log.info("Received login request packet: {}", loginRequestPacket.getUsername());
        LoginResponsePacket resp = new LoginResponsePacket();
        resp.setUsername(loginRequestPacket.getUsername());
        resp.setVersion(loginRequestPacket.getVersion());
        if (!StringUtil.isNullOrEmpty(loginRequestPacket.getUsername())) {
            if (LoginUtil.hasLogin(channel)) {
                log.warn("{}, You are already logged in", loginRequestPacket.getUsername());
                resp.setSuccess(false);
                resp.setCode(500);
                resp.setMessage("Already logged in");
            } else {
                String userId = RandUtil.randString(8);
                resp.setUserId(userId);
                // login success, save logged session info.
                SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUsername()), channel);
                log.info("User: {} login success", loginRequestPacket.getUsername());
                LoginUtil.markAsLogin(channel);
            }
        } else {
            log.warn("User login failed for null or empty username");
            resp.setMessage("Login Authentication Failed");
            resp.setSuccess(false);
            resp.setCode(500);
        }
        return resp;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
        super.channelInactive(ctx);
    }
}
