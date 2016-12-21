package net.galvin.orange.core.transport.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.galvin.orange.core.Utils.JDKSerializeUtils;
import net.galvin.orange.core.Utils.SysEnum;
import net.galvin.orange.core.demo.HelloServiceImpl;
import net.galvin.orange.core.transport.comm.NetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
        List<Byte> byteList = new ArrayList<Byte>();
        while (byteBufMsg.isReadable()){
            byteList.add(byteBufMsg.readByte());
        }
        if(byteList != null && byteList.size() > 0){
            byte[] byteArr = new byte[byteList.size()];
            for(int i=0;i<byteList.size();i++){
                byteArr[i] = byteList.get(i);
            }
            NetRequest<String> netRequest = (NetRequest<String>) JDKSerializeUtils.deserialize(byteArr);
            String returnVal = HelloServiceImpl.get().hello(netRequest.getBody());
        }

//        ByteBuf byteBuf = ctx.channel().alloc().buffer();
//        byteBuf.writeCharSequence(returnVal, CharsetUtil.UTF_8);
//        try {
//            ctx.channel().writeAndFlush(byteBuf).sync();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("NettyServerHandler ===>> exceptionCaught");
        logger.error(SysEnum.format(cause));
        ctx.close();
    }

}
