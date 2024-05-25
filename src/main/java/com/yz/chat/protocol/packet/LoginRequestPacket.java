package com.yz.chat.protocol.packet;

import static com.yz.chat.command.Command.LOGIN_REQUEST;

import com.yz.chat.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequestPacket extends Packet {
    private String userId;
    private String username;
    private String password;

    @Override
    public byte getCommand() {
        return LOGIN_REQUEST;
    }
}
