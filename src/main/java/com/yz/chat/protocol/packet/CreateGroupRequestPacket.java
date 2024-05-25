package com.yz.chat.protocol.packet;

import com.yz.chat.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

import static com.yz.chat.command.Command.CREATE_GROUP_REQUEST;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateGroupRequestPacket extends Packet {

    private String createUserId;
    private Set<String> userIdSet;
    private String groupName;

    @Override
    public byte getCommand() {
        return CREATE_GROUP_REQUEST;
    }
}
