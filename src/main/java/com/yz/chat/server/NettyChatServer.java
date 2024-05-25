package com.yz.chat.server;

import com.yz.chat.adapter.*;
import com.yz.chat.decoder.PacketDecoder;
import com.yz.chat.encoder.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyChatServer {
    private int port;

    public NettyChatServer(int port) {
        this.port = port;
    }

    void run() {
        ServerBootstrap server = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);

        ChannelFuture f = server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(new ProtocolAdapter());
                channel.pipeline().addLast(new StatisticDataAdapter());
                channel.pipeline().addLast(new PacketDecoder());
                channel.pipeline().addLast(new LoginRequestChannelAdapter());
                channel.pipeline().addLast(new AuthAdapter());
                channel.pipeline().addLast(new MessageRequestChannelAdapter());
                channel.pipeline().addLast(new CreateGroupRequestAdapter());
                channel.pipeline().addLast(new PacketEncoder());
            }
        }).bind(port);

        try {
            f.sync().await();
            f.channel().closeFuture().sync().await();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyChatServer(9000).run();
    }
}
