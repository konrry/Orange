package net.galvin.orange.core.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import net.galvin.orange.core.Utils.SysEnum;
import net.galvin.orange.core.demo.HelloServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("NettyServerHandler ===>> channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("NettyServerHandler ===>> channelRead");
        ByteBuf byteBufMsg = (ByteBuf) msg;
        StringBuffer msgBuffer = new StringBuffer();
        while(byteBufMsg.isReadable()){
            msgBuffer.append((char) byteBufMsg.readByte());
        }
        String returnVal = HelloServiceImpl.get().hello(msgBuffer.toString());

        ByteBuf byteBuf = ctx.channel().alloc().buffer();
        byteBuf.writeCharSequence(returnVal, CharsetUtil.UTF_8);
        try {
            ctx.channel().writeAndFlush(byteBuf).sync();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("NettyServerHandler ===>> exceptionCaught");
        logger.error(SysEnum.format(cause));
        ctx.close();
    }

}
