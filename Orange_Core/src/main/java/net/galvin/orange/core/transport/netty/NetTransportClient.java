package net.galvin.orange.core.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetTransportClient {

    private final static Logger logger = LoggerFactory.getLogger(NetTransportClient.class);


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
            ChannelFuture channelFuture = bootstrap.connect(nettySession.getRemoteAddress(),nettySession.getRemotePort()).sync();
            nettySession.setChannelFuture(channelFuture);
            ByteBuf byteBuf = nettySession.getChannelFuture().channel().alloc().buffer();
            byteBuf.writeCharSequence("000000", CharsetUtil.UTF_8);
            nettySession.getChannelFuture().channel().writeAndFlush(byteBuf);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
//            workerGroup.shutdownGracefully();
        }

    }

}