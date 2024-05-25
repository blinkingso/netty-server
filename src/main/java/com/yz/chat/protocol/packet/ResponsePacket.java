package com.yz.chat.protocol.packet;

import com.yz.chat.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class ResponsePacket<T extends ResponsePacket<T>> extends Packet {

    public ResponsePacket() {
        this.code = 200;
        this.message = "SUCCESS";
    }

    private int code;
    private boolean success = true;
    private String message;
    private T data;
}
