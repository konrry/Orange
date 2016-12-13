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

/**
 * ChannelHandler 有两个生命周期监听方法：handlerAdded() 和 handlerRemoved()。
 * 你可以执行任意的 初始化 任务，只要这个任务不会阻塞很久。
 * 首先所有接受到的数据 必须都累计保存在buf中。
 * 然后，handler 必须检查 buf 是否有足够的数据。在本例子中是4个字节，然后执行 实际的业务逻辑。
 * 另外，当接收到新的数据，netty将会再次调用 channelRead() 方法，实际上 所有的四个字节会被积累。
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyClientHandler ===>> channelActive");
        ctx.write(Unpooled.copiedBuffer("NettyClientHandler.channelActive", CharsetUtil.UTF_8));
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
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println("NettyClientHandler ===>> channelRead0");
        System.out.println("Client received: "+ ByteBufUtil.hexDump(in.readBytes(in.readableBytes())));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("NettyClientHandler ===>> exceptionCaught");
        cause.printStackTrace();
    }

}
