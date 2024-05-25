package com.yz.decoder;


import com.yz.chat.decoder.ByteMessageDecoder;
import com.yz.chat.decoder.StringMessageDecoder;
import junit.framework.TestCase;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DecoderTests extends TestCase {


    public void testByteMessageDecoder() {
        ByteMessageDecoder decoder = new ByteMessageDecoder();
        ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes(StandardCharsets.UTF_8));
        byte[] bytes = decoder.decode(buffer);
        assertEquals("hello world", new String(bytes, StandardCharsets.UTF_8));
        StringMessageDecoder stringMessageDecoder = new StringMessageDecoder();
        String message = stringMessageDecoder.decode(buffer);
        assertEquals("hello world", message);
    }
}
