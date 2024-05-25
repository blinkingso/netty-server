package com.yz.chat.protocol.packet;

import com.yz.chat.session.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

import static com.yz.chat.command.Command.CREATE_GROUP_RESPONSE;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateGroupResponsePacket extends ResponsePacket<CreateGroupResponsePacket> {

    private String groupId;
    private String groupName;
    private String createUserId;
    private Set<Session> groupSessions;

    @Override
    public byte getCommand() {
        return CREATE_GROUP_RESPONSE;
    }
}
