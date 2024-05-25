package com.yz.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final Set<Connection> connections = new HashSet<>();

    public ChatServer(InetSocketAddress inetSocketAddress) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(inetSocketAddress);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("Server started at {}", inetSocketAddress);
    }

    public Selector getSelector() {
        return selector;
    }

    public void run() throws IOException {
        new Thread(() -> {
            logger.info("ChatServer started at {}", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // epoll_wait return events count that can be processing
                    int events = selector.select();
                    if (events > 0) {
                        logger.debug("Received " + events + " events");
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        selectionKeys.forEach(key -> {
                            if (key.isAcceptable()) {
                                // New connection here.
                                ServerSocketChannel server = (ServerSocketChannel) key.channel();
                                try {
                                    SocketChannel socketChannel = server.accept();
                                    if (socketChannel != null) {
                                        socketChannel.configureBlocking(false);
                                        // waiting for user's login here.
                                        socketChannel.register(key.selector(), SelectionKey.OP_READ);
                                    }
                                } catch (IOException e) {
                                    logger.warn("Failed to accept connection", e);
                                }
                            }
                        });
                        selectionKeys.clear();
                    }
                } catch (IOException e) {
                    logger.warn("Selector caused an exception", e);
                }
            }
        }, "ChatServer").start();
    }

    public void shutdown() throws IOException {
        serverSocketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        new ChatServer(new InetSocketAddress("0.0.0.0", 8888)).run();
    }
}
