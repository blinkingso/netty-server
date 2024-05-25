package com.yz.chat.encoder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringMessageEncoder implements MessageEncoder<String> {
    @Override
    public ByteBuffer encode(String s) {
        return ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
    }
}
