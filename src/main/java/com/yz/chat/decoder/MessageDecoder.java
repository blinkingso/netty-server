package com.yz.chat.decoder;

import com.yz.chat.exception.ConvertException;

import java.nio.ByteBuffer;

public interface MessageDecoder<T> {

    T decode(ByteBuffer buffer) throws ConvertException;
}
