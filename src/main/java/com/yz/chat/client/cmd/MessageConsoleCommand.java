package com.yz.chat.client.cmd;

import com.yz.chat.protocol.MessageType;
import com.yz.chat.protocol.packet.MessageRequestPacket;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class MessageConsoleCommand implements ConsoleCommand {
    private final MessageType mType;

    public MessageConsoleCommand(MessageType mType) {
        this.mType = mType;
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        String prompt = "Please enter your message with format: [%s message]";
        if (mType.equals(MessageType.User)) {
            prompt = String.format(prompt, "userId");
        } else {
            prompt = String.format(prompt, "groupId");
        }
        log.info(prompt);
        String message = scanner.nextLine();
        log.info("next message: {}", message);
        String[] msg = message.split(" ", 2);
        if (msg.length != 2) {
            log.warn("invalid message format: {}", message);
            this.exec(scanner, channel);
            return;
        }
        String userId = msg[0].trim();
        String msgContent = msg[1].trim();
        MessageRequestPacket packet = new MessageRequestPacket();
        packet.setMType(mType);
        packet.setToUserId(userId);
        packet.setMessage(msgContent);
        channel.writeAndFlush(packet);
    }
}
