package com.yz.chat.protocol.packet;

import com.yz.chat.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.yz.chat.command.Command.MESSAGE_RESPONSE;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageResponsePacket extends Packet {

    private String fromUserId;
    private String fromUserName;
    private String message;

    @Override
    public byte getCommand() {
        return MESSAGE_RESPONSE;
    }
}
