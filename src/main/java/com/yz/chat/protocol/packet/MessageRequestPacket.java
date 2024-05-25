package com.yz.chat.protocol.packet;

import com.yz.chat.protocol.MessageType;
import com.yz.chat.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.yz.chat.command.Command.MESSAGE_REQUEST;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageRequestPacket extends Packet {

    private MessageType mType;
    private String toUserId;
    private String message;

    @Override
    public byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
