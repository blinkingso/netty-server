package com.yz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static java.lang.Thread.sleep;

public class NioClient {
    public static void main(String[] args) {
        try {
            SocketChannel socket = SocketChannel.open();
            socket.configureBlocking(false);
            socket.connect(new InetSocketAddress("127.0.0.1", 8888));
            while (!socket.finishConnect()) {
                sleep(100);
            }
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1200);
                socket.write(ByteBuffer.wrap("Hello World".getBytes()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
