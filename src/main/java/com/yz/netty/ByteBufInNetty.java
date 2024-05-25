package com.yz.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.nio.charset.StandardCharsets;

public class ByteBufInNetty {
    public static void main(String[] args) {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(1024, 2048);
        buf.writeBytes("hello world".getBytes(StandardCharsets.UTF_8));
        int ri = buf.readerIndex();
        System.out.println(ri);
        System.out.println(buf.writerIndex());
        System.out.println(buf.isReadable());
        buf.writeBytes("New words".getBytes(StandardCharsets.UTF_8));
        byte[] hello = new byte[5];
        buf.readBytes(hello);
        System.out.println(new String(hello, StandardCharsets.UTF_8));
        System.out.println(buf.readerIndex());
        buf.retain();
        boolean release = buf.release();
        if (release) {
            System.out.println("Buf released.");
        }
        System.out.println(buf);

        ByteBuf slice = buf.slice(1,4);
        System.out.println(slice.toString(StandardCharsets.UTF_8));
    }
}
