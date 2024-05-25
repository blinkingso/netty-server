package com.yz.chat.handler;

import com.yz.chat.protocol.Packet;
import com.yz.chat.protocol.ProtocolCodeC;
import com.yz.chat.protocol.packet.LoginResponsePacket;
import com.yz.chat.protocol.packet.MessageRequestPacket;
import com.yz.chat.serialize.Serializer;
import com.yz.chat.utils.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class LoginResponseHandler implements PacketHandler {

    @Override
    public void handle(Packet packet, Serializer serializer, Channel channel) {
        LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
        if (loginResponsePacket.isSuccess()) {
            LoginUtil.markAsLogin(channel);
            log.info("Login success");
            // start a new thread to control console input.
            startConsoleThread(channel);
        } else {
            log.warn("Login failed, please retry later");
        }
    }

    private void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    log.info("Enter message and press enter to send!");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMessage(line);
                    ByteBuf buf = ProtocolCodeC.INSTANCE.encode(channel.alloc(), Serializer.DEFAULT, packet);
                    channel.writeAndFlush(buf);
                }
            }
        }, "ClientConsoleThread").start();
    }
}
