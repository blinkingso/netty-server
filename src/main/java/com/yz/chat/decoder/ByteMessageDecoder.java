package com.yz.chat.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteMessageDecoder implements MessageDecoder<byte[]> {
    private static final Logger logger = LoggerFactory.getLogger(ByteMessageDecoder.class);

    @Override
    public byte[] decode(ByteBuffer buffer) {
        byte[] array = buffer.array();
        logger.debug("Received bytes: {}", Arrays.toString(array));
        return array;
    }
}
