package com.yz.chat.decoder;

import com.yz.chat.exception.ConvertException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringMessageDecoder implements MessageDecoder<String> {
    private final ByteMessageDecoder byteMessageDecoder = new ByteMessageDecoder();

    @Override
    public String decode(ByteBuffer buffer) throws ConvertException {
        byte[] bufferedBytes = byteMessageDecoder.decode(buffer);
        try {
            return new String(bufferedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new ConvertException("Failed to parse bytes to String", e);
        }
    }
}
