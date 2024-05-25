package com.yz.chat.client.cmd;

import io.netty.channel.Channel;

import java.util.Scanner;

@FunctionalInterface
public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);
}
