package com.yz.netty;

import com.yz.netty.handler.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

public class NettyClient2 {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new InBoundA());
                ch.pipeline().addLast(new InBoundB());
                ch.pipeline().addLast(new InBoundC());
                // out
                ch.pipeline().addLast(new OutBoundA());
                ch.pipeline().addLast(new OutBoundB());
                ch.pipeline().addLast(new OutBoundC());
            }
        });
        Channel channel = bootstrap.connect("127.0.0.1", 8888).channel();
        channel.closeFuture().sync().await();
    }
}
