package net.galvin.orange.core.transport.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.galvin.orange.core.Utils.JDKSerializeUtils;
import net.galvin.orange.core.Utils.MessageUtils;
import net.galvin.orange.core.Utils.SysEnum;
import net.galvin.orange.core.demo.HelloServiceImpl;
import net.galvin.orange.core.transport.comm.MessageCodec;
import net.galvin.orange.core.transport.comm.NetRequest;
import net.galvin.orange.core.transport.comm.NetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    MessageCodec<NetRequest<String>> messageCodec = new MessageCodec<NetRequest<String>>();

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        logger.debug("NettyServerHandler ===>> channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.debug("NettyServerHandler ===>> channelRead");
        ByteBuf byteBufMsg = (ByteBuf) msg;
        boolean status = messageCodec.codec(byteBufMsg);
        if(status){
            NetRequest<String> netRequest = messageCodec.getT();
            String returnVal = HelloServiceImpl.get().hello(netRequest.getBody());

            NetResponse<String> netResponse = new NetResponse<String>(netRequest.getRequestId(),returnVal);
            byte[] netResponseByteArr = JDKSerializeUtils.serialize(netResponse);
            byte[] messageByteArr = MessageUtils.buildNetMessage(netResponseByteArr);
            ByteBuf byteBuf = ctx.channel().alloc().buffer();
            byteBuf.writeBytes(messageByteArr);
            try {
                ctx.channel().writeAndFlush(byteBuf).sync();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            this.messageCodec.reset();
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.debug("NettyServerHandler ===>> exceptionCaught");
        logger.error(SysEnum.format(cause));
        ctx.close();
    }

}
