package com.yz.chat.protocol.packet;

import com.yz.chat.command.Command;
import com.yz.chat.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JoinGroupRequestPacket extends Packet {
    private String groupId;

    @Override
    public byte getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }
}
