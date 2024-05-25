package com.yz.chat.protocol;

import lombok.Data;

@Data
public abstract class Packet {

    private byte version = 1;

    public abstract byte getCommand();
}
