package net.galvin.orange.core.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.galvin.orange.core.demo.HelloServiceImpl;

public class NettyTransportServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("NettyTransportServerHandler ===>> channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("NettyTransportServerHandler ===>> channelRead");
        System.out.println("Server received: "+msg);
        ctx.writeAndFlush(msg);
        HelloServiceImpl.get().hello("serverChannelRead");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyTransportServerHandler ===>> channelReadComplete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("NettyTransportServerHandler ===>> exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }

}
