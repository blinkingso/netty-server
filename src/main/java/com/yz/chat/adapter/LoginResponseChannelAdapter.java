package com.yz.chat.adapter;

import com.yz.chat.client.cmd.ConsoleCommand;
import com.yz.chat.client.cmd.ConsoleCommandManager;
import com.yz.chat.client.cmd.LoginConsoleCommand;
import com.yz.chat.protocol.packet.LoginResponsePacket;
import com.yz.chat.session.Session;
import com.yz.chat.session.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class LoginResponseChannelAdapter extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        if (loginResponsePacket.isSuccess()) {
            SessionUtil.bindSession(new Session(loginResponsePacket.getUserId(), loginResponsePacket.getUsername()), ctx.channel());
            log.info("Login success with userId: {}", loginResponsePacket.getUserId());
            startConsoleThread(ctx.channel());
        } else {
            log.warn("Login failed for: {}", loginResponsePacket.getMessage());
        }
    }

    private void startConsoleThread(Channel channel) {
        final Scanner sc = new Scanner(System.in);
        final ConsoleCommand manager = new ConsoleCommandManager();
        final ConsoleCommand loginCommand = new LoginConsoleCommand();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (SessionUtil.hasLogin(channel)) {
                    log.info("Please enter your command: \n\tcreateGroup \n\tjoinGroup \n\tsendToUser \n\tsendToGroup \n\tlogout");
                    manager.exec(sc, channel);
                } else {
                    loginCommand.exec(sc, channel);
                }
            }
        }, "ClientConsoleThread").start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }

}
