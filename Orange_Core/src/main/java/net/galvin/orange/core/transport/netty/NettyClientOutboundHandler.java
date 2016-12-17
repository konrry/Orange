package net.galvin.orange.core.transport.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import net.galvin.orange.core.Utils.SysEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class NettyClientOutboundHandler extends ChannelOutboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(NettyClientOutboundHandler.class);

    private ChannelHandlerContext channelHandlerContext = null;

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        super.connect(ctx, remoteAddress, localAddress, promise);
        this.channelHandlerContext = null;
        System.out.println("NettyClientOutboundHandler ===>> connect");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("NettyClientOutboundHandler ===>> exceptionCaught");
        this.channelHandlerContext = null;
        logger.error(SysEnum.format(cause));
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }
}
