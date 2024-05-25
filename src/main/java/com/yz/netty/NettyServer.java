package com.yz.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // bossGroup表示监听端口，workerGroup表示处理每一个连接的数据读写的线程组。
        EpollEventLoopGroup bossGroup = new EpollEventLoopGroup(1);
        EpollEventLoopGroup workerGroup = new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        ChannelFuture future = serverBootstrap.group(bossGroup, workerGroup)
                // 基于linux的epoll模型。更高的性能（相比NioServerSocketChannel.class(对于java中nio的封装))
                .channel(EpollServerSocketChannel.class)
                // 用于指定在服务端启动过程中的一些逻辑， 通常情况下用不到这个方法
                .handler(new ChannelHandler() {
                    @Override
                    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

                    }

                    @Override
                    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

                    }
                })
                // 给NioServerSocketChannel指定一些自定义属性，然后通过channel.attr()取出这个属性。
                .attr(AttributeKey.newInstance("author"), "mu")
                // 给每一个连接(SocketChannel)指定自定义属性。
                .childAttr(AttributeKey.newInstance("clientKey"), "clientValue")
                // 给服务端Channel设置一些TCP参数， 最常见的就是so_backlog 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，
                // 如果连接建立频繁，服务器处理创建新连接较慢， 则可以适当调大这个参数。
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 给每一个连接设置tcp参数。
                // so_keepalive 表示是否开启tcp底层心跳机制， true表示开启
                // tcp_nodelay 表示是否开启Nagle算法， true表示关闭， false表示开启。 即： 如果要求高实时性，有数据时就马上发送，就设置为关闭
                // 如果需要减少发送次数， 减少网络交互， 就设置为开启（false）
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 对于每一个连接的数据读写处理流程
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FirstServerHandler());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                LOGGER.info("Got Message: {}", msg);
                            }
                        });
                    }
                }).bind(8888);
        try {
            future.addListener(f -> {
                if (f.isSuccess()) {
                    LOGGER.info("Server started on port 8888");
                } else {
                    LOGGER.error("Server failed to start on port 8888");
                }
            });
            future.sync().await();
            future.channel().closeFuture().sync().await();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
