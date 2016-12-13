package net.galvin.orange.core.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.galvin.orange.core.demo.HelloServiceImpl;

/**
 * ChannelInboundHandlerAdapter 实现了 ChannelInboundHandler，提供了多种事件处理方法。
 */
public class NettyTransportServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当一个链接建立准备通信时，方法 channelActive() 会被调用。
     * 我们写了一个 32位的整数代表方法中的当前时间。
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("NettyTransportServerHandler ===>> channelActive");
        /**
         * 为了发送一个消息，我们需要分配一个新的 buffer 来容纳消息。
         * 我们将会写一个32位的证书，所以我们需要的容量是4个字节。
         * 通过 ChannelHandlerContext.alloc() 可以获取 ByteBufAllocator，分配一个buffer。
         */
        final ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes("server.channelActive".getBytes());
        /**
         * 和平常一样，我们写了一个结构化的消息。
         * 等一下，flip在哪里？难道我们在NIO中发消息之前不需要调用方法 java.nio.ByteBuffer.flip()。
         * ByteBuf 没有这样的方法，因为它有两个指针。一个负责读操作，另外一个负责写操作。
         * 当你往 ByteBuf 中写东西的时候，写的脚标会自动增长，读的脚标不会改变。
         * 当消息开始和结束的时候，写和读的脚标都会重置。
         * NIO buffer 不能很清楚的说出 消息内容的开始和结束位置，除非调用方法 flip。
         * 忘记 flip 这个 buffer，会让人很麻烦，因为没有或者错误的数据会被发出去。
         * 这个样的错误不会再netty中发生， 因为不同的操作类型有不同的指针。
         * 注意到 方法 ChannelHandlerContext.write() (and writeAndFlush()) 会返回 对象 ChannelFuture。
         * ChannelFuture代表一个 I/O 操作还没有被执行
         * 也就是所有的请求操作也许还没有被执行，因为netty中所有的操作都是异步的。
         * 举个例子，下面的例子中，连接关闭的时候，消息有可能还没有发出去。
         * Channel ch = ...;
         * ch.writeAndFlush(message);
         * ch.close();
         * 因此你需要在 ChannelFuture 完成之后，调用 close() 方法，会通知监听器 写的操作已经结束了。
         * 请注意，close() 不会立即管理当前的连接，它也会返回一个 ChannelFuture。
         */
        try {
            final ChannelFuture f = ctx.writeAndFlush(byteBuf);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 当一个写的请求结束之后，怎么获取到通知。
         * 简单的增加了一个 ChannelFutureListener。
         * 这里我们创建了一个匿名的监听器，当操作完成之后操作关闭。
         */
//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) {
//                assert f == future;
//                ctx.close();
//            }
//        });
    }

    /**
     * 这里我们覆盖了时间处理的方法：channelRead()
     * 当接收到客户端的消息的时候，这个方法会被调用。
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("NettyTransportServerHandler ===>> channelRead");
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
                System.out.flush();
            }
            HelloServiceImpl.get().hello(null);
        } finally {
            /**
             * ByteBuf是有一个引用计数的对象，必须要显示的调用 release() 方法来释放。
             * 记住handler的在职责就是释放 任何传给handler的应用对象
             */
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 由于I/O的错误或者处理器实现中报错，该方法都会被调用。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("NettyTransportServerHandler ===>> exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }

}
