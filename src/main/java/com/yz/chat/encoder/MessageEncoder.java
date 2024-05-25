package com.yz.chat.encoder;

import java.nio.ByteBuffer;

public interface MessageEncoder<T> {

    ByteBuffer encode(T t);
}
