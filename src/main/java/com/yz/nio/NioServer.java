package com.yz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) {
        try {
            final ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            final Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            serverChannel.bind(new InetSocketAddress(8888));

            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    // 监听事件
                    try {
                        int events = selector.select();
                        if (events > 0) {
                            Set<SelectionKey> selectionKeys = selector.selectedKeys();
                            for (SelectionKey key : selectionKeys) {
                                if (key.isAcceptable()) {
                                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                                    SocketChannel socket = server.accept();
                                    if (socket != null) {
                                        socket.configureBlocking(false);
                                        socket.register(key.selector(), SelectionKey.OP_READ);
                                    }
                                } else if (key.isReadable()) {
                                    // read from socket channel
                                    SocketChannel socket = (SocketChannel) key.channel();
                                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                                    int length = socket.read(buffer);
                                    if (length > 0) {
                                        buffer.flip();
                                        System.out.println(Charset.defaultCharset().decode(buffer));
                                        socket.register(key.selector(), SelectionKey.OP_WRITE);
                                    }
                                } else if (key.isWritable()) {
                                    SocketChannel socket = (SocketChannel) key.channel();
                                    // write to socket channel
                                    socket.register(key.selector(), SelectionKey.OP_READ);
                                }

                            }
                            selectionKeys.clear();
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to process events: " + e);
                    }
                }
                try {
                    serverChannel.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, "Server-Thread").start();
        } catch (IOException e) {
            System.out.println("Failed to create server channel");
        }
    }
}
