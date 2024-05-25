package com.yz.chat.encoder;

import java.nio.ByteBuffer;

public class ByteMessageEncoder implements MessageEncoder<byte[]>{
    @Override
    public ByteBuffer encode(byte[] bytes) {
        return ByteBuffer.wrap(bytes);
    }
}
