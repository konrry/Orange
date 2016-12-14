package net.galvin.orange.core.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyClientHandler ===>> channelActive");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("NettyClientHandler ===>> handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("NettyClientHandler ===>> handlerRemoved");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("NettyClientHandler ===>> channelRead");
        ByteBuf byteBufMsg = (ByteBuf) msg;
        while(byteBufMsg.isReadable()){
            System.out.println(byteBufMsg.readByte());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("NettyClientHandler ===>> exceptionCaught");
        cause.printStackTrace();
    }

}
