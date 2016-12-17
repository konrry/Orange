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
import net.galvin.orange.core.Utils.SysEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class NettyClient {

    private final static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;
    private final NettyClientInboundHandler nettyClientInboundHandler;
    private final NettyClientOutboundHandler nettyClientOutboundHandler;

    public static NettyClient newInstance(NettySession nettySession){
        NettyClient nettyClient = new NettyClient(nettySession);
        nettySession.setNettyClient(nettyClient);
        return nettyClient;
    }

    private NettyClient(NettySession nettySession){
        nettyClientInboundHandler = new NettyClientInboundHandler();
        nettyClientOutboundHandler = new NettyClientOutboundHandler();
        init(nettySession);
    }

    /**
     * 初始化客户端
     * @param nettySession
     */
    private void init(NettySession nettySession){

        this.workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(nettyClientInboundHandler)
                            .addLast(nettyClientOutboundHandler);
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

    /**
     * 发消息
     * @param msg
     */
    public void writeAndFlush(String msg){
        ByteBuf byteBuf = this.channelFuture.channel().alloc().buffer();
        byteBuf.writeCharSequence(msg, CharsetUtil.UTF_8);
        try {
            this.channelFuture.channel().writeAndFlush(byteBuf).sync();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public String read(){
        String msg = null;
        try {
            this.channelFuture.get();
            msg = this.nettyClientInboundHandler.getResultVal();
        } catch (Exception e) {
            logger.error(SysEnum.format(e));
        }
        return msg;
    }

}