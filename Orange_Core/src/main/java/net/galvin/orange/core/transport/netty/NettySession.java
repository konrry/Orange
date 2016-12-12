package net.galvin.orange.core.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.Date;

/**
 * Created by qchu on 16-12-11.
 */
public class NettySession {

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

    public void writeAndFlush(byte[] data){

        if(this.channelFuture == null){
            synchronized (this){
                if(this.channelFuture == null){
                    NetTransportClient.newInstance(this);
                }
            }
        }
        ByteBuf byteBuf = this.channelFuture.channel().alloc().buffer();
        byteBuf.writeBytes(data);
        try {
            this.channelFuture.channel().writeAndFlush(byteBuf).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.channelFuture.channel().read();

    }

    public void close(){
        this.channelFuture.channel().closeFuture();
    }

}
