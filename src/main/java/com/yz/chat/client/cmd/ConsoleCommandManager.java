package com.yz.chat.client.cmd;

import com.yz.chat.protocol.MessageType;
import com.yz.chat.protocol.packet.CreateGroupRequestPacket;
import com.yz.chat.protocol.packet.JoinGroupRequestPacket;
import com.yz.chat.session.Session;
import com.yz.chat.session.SessionUtil;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class ConsoleCommandManager implements ConsoleCommand {

    private final Map<String, ConsoleCommand> commands = new ConcurrentHashMap<>();

    public static final ConsoleCommand SEND_TO_USER = new MessageConsoleCommand(MessageType.User);

    public static final ConsoleCommand CREATE_GROUP = (scanner, channel) -> {
        log.info("Please enter your group name:");
        String groupName = scanner.nextLine();
        if (StringUtil.isNullOrEmpty(groupName)) {
            return;
        }
        log.info("Please enter your group ids, split with comma:");
        String ids = scanner.nextLine();
        if (StringUtil.isNullOrEmpty(ids)) {
            return;
        }

        String[] idArray = ids.split(",");
        if (idArray.length == 0) {
            log.warn("A group at least has more than one user.");
            return;
        }
        Set<String> idSet = Arrays.stream(idArray).map(String::trim).filter(s -> !StringUtil.isNullOrEmpty(s)).collect(Collectors.toSet());
        CreateGroupRequestPacket packet = new CreateGroupRequestPacket();
        packet.setGroupName(groupName);
        packet.setUserIdSet(idSet);
        Session session = SessionUtil.getSession(channel);
        packet.setCreateUserId(session.getUserId());
        channel.writeAndFlush(packet);
    };
    public static final ConsoleCommand JOIN_GROUP = (scanner, channel) -> {
        log.info("Enter group id to join it: ");
        String groupId = scanner.nextLine();
        JoinGroupRequestPacket packet = new JoinGroupRequestPacket();
        packet.setGroupId(groupId);
        channel.writeAndFlush(packet);
    };
    public static final ConsoleCommand SEND_TO_GROUP = new MessageConsoleCommand(MessageType.Group);

    public static final ConsoleCommand LOGOUT = (scanner, channel) -> {

    };

    public ConsoleCommandManager() {
        commands.put("login", new LoginConsoleCommand());
        commands.put("sendToUser", SEND_TO_USER);
        commands.put("createGroup", CREATE_GROUP);
        commands.put("sendToGroup", SEND_TO_GROUP);
        commands.put("joinGroup", JOIN_GROUP);
        commands.put("logout", LOGOUT);
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        String command = scanner.nextLine();
        ConsoleCommand cmd = commands.get(command);
        if (cmd == null) {
            log.warn("Unknown command: {}", command);
        } else {
            cmd.exec(scanner, channel);
        }
    }
}
