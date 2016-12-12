package net.galvin.orange.core.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty服务器
 */
public class NettyTransportServer {


    private int port = 9028;

    public void start(){
        /**
         * EventLoopGroup 是一个多线程的处理I/O操作的时间循环。对于不同种类的传输，netty提供了多种EventLoopGroup.
         * bossGroup主要负责连接的接入，接受连接注册到workerGroup。
         * 有多少个线程会被使用，怎么映射到channel，都依赖EventLoopGroup的实现。甚至可以通过构造器来实现。
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /**
             * ServerBootstrap可以帮助我们来设置一个服务器。
             * 你也可以直接使用Channel来设置服务器，这非常的多余。大多数情况下我们没有必要这么做。
             */
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    /**
                     * 当接收到一个新的通道，才会指定一个handler
                     * ChannelInitializer 是一个特殊的处理器，意图是帮助用户配置一个新的 Channel
                     * 为了实现网络应用，你很有可能要为一个新的 Channel 增加一些 handlers
                     * 当应用越来越复杂的时候，你很有可能增加更多的处理器并且抽取出匿名类。
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyTransportServerHandler());
                        }
                    })
                    /**
                     * 你可以为 Channel 的实现设置参数。
                     * 我们正在写 TCP/IP 服务，所以我们允许为 socket 设置选项，譬如 tcpNoDelay 和 keepAlive
                     *
                     * [backlog]的定义是已连接但未进行accept处理的SOCKET队列大小，已是（并非syn的SOCKET队列）。
                     * 如果这个队列满了，将会发送一个ECONNREFUSED错误信息给到客户端,
                     * 即 linux 头文件 /usr/include/asm-generic/errno.h中定义的“Connection refused”，
                     * （如果协议不支持重传，该请求会被忽略）
                     */
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        NettyTransportServer nettyTransportServer = new NettyTransportServer();
        nettyTransportServer.start();
    }

}
