package net.galvin.orange.core.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class NettySession {

    private Logger logger = LoggerFactory.getLogger(NettySession.class);

    private String remoteAddress;
    private Integer remotePort;
    private ChannelFuture channelFuture;
    private final Date createTime;

    public NettySession(String remoteAddress, Integer remotePort) {
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
        this.createTime = new Date();
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public void writeAndFlush(String msg){

        if(this.channelFuture == null){
            synchronized (this){
                if(this.channelFuture == null){
                    NetTransportClient.newInstance(this);
                }
            }
        }
        System.out.println("......");
        ByteBuf byteBuf = this.channelFuture.channel().alloc().buffer();
        byteBuf.writeCharSequence(msg, CharsetUtil.UTF_8);
        try {
            this.channelFuture.channel().writeAndFlush(byteBuf).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getReturnVal(){
        Object returnVal = null;
        try {
            returnVal = this.channelFuture.get();
        } catch (Exception e) {

        }
        return returnVal;
    }


    public void close(){
        this.channelFuture.channel().closeFuture();
    }

}
