package net.galvin.orange.core.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NetTransportClient {


    public static void newInstance(NettySession nettySession){

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final NettyClientHandler nettyClientHandler = new NettyClientHandler();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(nettyClientHandler);
                }
            });

            // Start the client.
            ChannelFuture channelFuture = bootstrap
                    .connect(nettySession.getRemoteAddress(), nettySession.getRemotePort()).sync();
            nettySession.setChannelFuture(channelFuture);
            // Wait until the connection is closed.
            channelFuture.channel().closeFuture();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}