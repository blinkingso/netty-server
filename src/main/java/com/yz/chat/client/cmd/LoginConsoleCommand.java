package com.yz.chat.client.cmd;

import com.yz.chat.protocol.packet.LoginRequestPacket;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class LoginConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        log.info("Please enter your username:");
        String username = scanner.nextLine();
        if (StringUtil.isNullOrEmpty(username)) {
            this.exec(scanner, channel);
            return;
        }
        username = username.trim();
        log.info("Please enter your password:");
        String password = scanner.nextLine();
        if (StringUtil.isNullOrEmpty(password)) {
            this.exec(scanner, channel);
            return;
        }
        password = password.trim();
        // create login request
        LoginRequestPacket request = new LoginRequestPacket();
        request.setUsername(username);
        request.setPassword(password);
        channel.writeAndFlush(request);
    }
}
