package com.yz.chat.protocol.packet;

import com.yz.chat.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponsePacket extends ResponsePacket<LoginResponsePacket> {

    private String userId;
    private String username;

    @Override
    public byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}

