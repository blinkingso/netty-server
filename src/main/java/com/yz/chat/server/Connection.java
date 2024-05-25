package com.yz.chat.server;

import com.yz.chat.decoder.MessageDecoder;
import com.yz.chat.decoder.StringMessageDecoder;
import com.yz.chat.encoder.MessageEncoder;
import com.yz.chat.encoder.StringMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Connection {
    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private InetSocketAddress remoteAddress;
    private SocketChannel socketChannel;
    // buffer used to read or write data.
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final MessageDecoder<String> messageDecoder = new StringMessageDecoder();
    private final MessageEncoder<String> messageEncoder = new StringMessageEncoder();

    public Connection(SocketChannel socketChannel) {
        try {
            SocketAddress ra = socketChannel.getRemoteAddress();
            if (ra instanceof InetSocketAddress isa) {
                this.remoteAddress = isa;
            }
        } catch (IOException e) {
            // channel closed or channel IO error here.
            logger.error("Failed to get remote address", e);
        }
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void write(String message) throws IOException {
        ByteBuffer bufferedMessage = this.messageEncoder.encode(message);
        this.socketChannel.write(bufferedMessage);
    }

    public void read() throws IOException {
        int length = this.socketChannel.read(buffer);
        if (length > 0) {

        }
    }
}
