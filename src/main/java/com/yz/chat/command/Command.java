package com.yz.chat.command;

import com.yz.chat.protocol.Packet;
import com.yz.chat.protocol.packet.*;

public interface Command {
    byte LOGIN_REQUEST = 1;
    byte LOGIN_RESPONSE = 2;
    byte MESSAGE_REQUEST = 3;
    byte MESSAGE_RESPONSE = 4;
    byte CREATE_GROUP_REQUEST = 5;
    byte CREATE_GROUP_RESPONSE = 6;
    byte JOIN_GROUP_REQUEST = 7;
    byte JOIN_GROUP_RESPONSE = 8;


    static Class<? extends Packet> getPacketType(byte command) {
        return switch (command) {
            case LOGIN_REQUEST -> LoginRequestPacket.class;
            case LOGIN_RESPONSE -> LoginResponsePacket.class;
            case MESSAGE_REQUEST -> MessageRequestPacket.class;
            case MESSAGE_RESPONSE -> MessageResponsePacket.class;
            case CREATE_GROUP_REQUEST -> CreateGroupRequestPacket.class;
            case CREATE_GROUP_RESPONSE -> CreateGroupResponsePacket.class;
            default -> null;
        };
    }
}
