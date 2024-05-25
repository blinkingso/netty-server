package com.yz.chat.client;

import com.yz.chat.client.cmd.ConsoleCommand;
import com.yz.chat.client.cmd.LoginConsoleCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final ConsoleCommand login = new LoginConsoleCommand();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // create login request.
        Scanner scanner = new Scanner(System.in);
        login.exec(scanner, ctx.channel());

        super.channelActive(ctx);
    }

}
