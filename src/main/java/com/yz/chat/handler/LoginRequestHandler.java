package com.yz.chat.handler;

import com.yz.chat.protocol.Packet;
import com.yz.chat.protocol.ProtocolCodeC;
import com.yz.chat.protocol.packet.LoginRequestPacket;
import com.yz.chat.protocol.packet.LoginResponsePacket;
import com.yz.chat.serialize.Serializer;
import com.yz.chat.utils.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginRequestHandler implements PacketHandler {

    @Override
    public void handle(Packet packet, Serializer serializer, Channel channel) {
        LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
        log.info("Received Login Request Packet: {}", loginRequestPacket.getUsername());
        LoginResponsePacket resp = new LoginResponsePacket();
        resp.setVersion(packet.getVersion());
        if (!StringUtil.isNullOrEmpty(loginRequestPacket.getUsername())) {
            if (LoginUtil.hasLogin(channel)) {
                log.warn("{}, You are already logged in", loginRequestPacket.getUsername());
                resp.setSuccess(false);
                resp.setCode(500);
                resp.setMessage("Already logged in");
            } else {
                log.info("User: {} login success", loginRequestPacket.getUsername());
                LoginUtil.markAsLogin(channel);
            }
        } else {
            log.warn("User login failed for null or empty username");
            resp.setMessage("Login Authentication Failed");
            resp.setSuccess(false);
            resp.setCode(500);
        }
        ByteBuf respBuf = ProtocolCodeC.INSTANCE.encode(channel.alloc(), serializer, resp);
        channel.writeAndFlush(respBuf);
    }
}

