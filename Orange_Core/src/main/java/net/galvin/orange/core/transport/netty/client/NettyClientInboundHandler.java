package net.galvin.orange.core.transport.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.galvin.orange.core.Utils.SysEnum;
import net.galvin.orange.core.transport.comm.MessageCodec;
import net.galvin.orange.core.transport.comm.NetResponse;
import net.galvin.orange.core.transport.comm.NetTransportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientInboundHandler extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(NettyClientInboundHandler.class);
    private ChannelHandlerContext channelHandlerContext = null;

    MessageCodec<NetResponse<String>> netResponseMessageCodec = new MessageCodec<NetResponse<String>>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("NettyClientInboundHandler ===>> channelActive");
        this.channelHandlerContext = ctx;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        logger.debug("NettyClientInboundHandler ===>> handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        logger.debug("NettyClientInboundHandler ===>> handlerRemoved");
        this.channelHandlerContext = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.debug("NettyClientInboundHandler ===>> channelRead");
        ByteBuf byteBufMsg = (ByteBuf) msg;
        boolean status = netResponseMessageCodec.codec(byteBufMsg);
        if(status){
            NetResponse<String> netResponse = netResponseMessageCodec.getT();
            NetTransportManager.notify(netResponse.getRequestId(),netResponse);
        }
        this.netResponseMessageCodec.reset();
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.debug("NettyClientInboundHandler ===>> exceptionCaught");
        logger.error(SysEnum.format(cause));
        this.channelHandlerContext = null;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

}
