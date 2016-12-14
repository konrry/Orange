package net.galvin.orange.core.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.galvin.orange.core.demo.HelloServiceImpl;

public class NettyTransportServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("NettyTransportServerHandler ===>> channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("NettyTransportServerHandler ===>> channelRead");
        ByteBuf byteBufMsg = (ByteBuf) msg;
        StringBuffer msgBuffer = new StringBuffer();
        while(byteBufMsg.isReadable()){
            msgBuffer.append((char) byteBufMsg.readByte());
        }
        System.out.println("msgBuffer: "+msgBuffer);
        String returnVal = HelloServiceImpl.get().hello(msgBuffer.toString());
        System.out.println("returnVal: "+returnVal);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("NettyTransportServerHandler ===>> exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }

}
