package com.yz.chat.client;

import com.yz.chat.adapter.CreateGroupResponseAdapter;
import com.yz.chat.adapter.LoginResponseChannelAdapter;
import com.yz.chat.adapter.MessageResponseChannelAdapter;
import com.yz.chat.adapter.ProtocolAdapter;
import com.yz.chat.decoder.PacketDecoder;
import com.yz.chat.encoder.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatClient {

    Bootstrap bootstrap;
    NioEventLoopGroup group;

    public ChatClient() {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
    }

    public void start(String host, int port) {
        ChannelFuture f = this.bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ProtocolAdapter());
                socketChannel.pipeline().addLast(new ClientHandler());
                socketChannel.pipeline().addLast(new PacketDecoder());
                socketChannel.pipeline().addLast(new LoginResponseChannelAdapter());
                socketChannel.pipeline().addLast(new CreateGroupResponseAdapter());
                socketChannel.pipeline().addLast(new MessageResponseChannelAdapter());
                socketChannel.pipeline().addLast(new PacketEncoder());
            }
        }).connect(host, port);
        try {
            f.sync().await();
            f.channel().closeFuture().await();
        } catch (InterruptedException e) {
            this.group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ChatClient().start("127.0.0.1", 9000);
    }
}
