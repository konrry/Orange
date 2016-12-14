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
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyTransportClient {

    private final static Logger logger = LoggerFactory.getLogger(NettyTransportClient.class);

    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;

    public static NettyTransportClient newInstance(NettySession nettySession){
        NettyTransportClient nettyTransportClient = new NettyTransportClient();
        nettyTransportClient.init(nettySession);
        return nettyTransportClient;
    }


    private void init(NettySession nettySession){
        this.workerGroup = new NioEventLoopGroup();
        final NettyTransportClientHandler nettyClientHandler = new NettyTransportClientHandler();
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
            this.channelFuture = bootstrap.connect(nettySession.getRemoteAddress(),nettySession.getRemotePort()).sync();
        } catch (Exception e) {
            logger.error(e.getMessage());
            workerGroup.shutdownGracefully();
        }
    }

    public void shutdownGracefully(){
        try {
            if(this.channelFuture != null){
                channelFuture.channel().closeFuture().sync();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            if(this.workerGroup != null){
                workerGroup.shutdownGracefully();
            }
        }
    }

    public void writeAndFlush(String msg){
        ByteBuf byteBuf = this.channelFuture.channel().alloc().buffer();
        byteBuf.writeCharSequence(msg, CharsetUtil.UTF_8);
        try {
            this.channelFuture.channel().writeAndFlush(byteBuf).sync();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}