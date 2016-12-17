package net.galvin.orange.core.transport.netty;

import java.util.Date;

public class NettySession {

    private String remoteAddress;
    private Integer remotePort;
    private final Date createTime;

    private NettyClient nettyClient;

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

    public Date getCreateTime() {
        return createTime;
    }

    public NettyClient getNettyClient() {
        return nettyClient;
    }

    public void setNettyClient(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public String writeAndFlush(String msg){
        if(this.nettyClient == null){
            synchronized (this){
                if(this.nettyClient == null){
                    NettyClient.newInstance(this);
                }
            }
        }
        this.nettyClient.writeAndFlush(msg);
        return this.nettyClient.read();
    }

    public void shutdownGracefully(){
        this.nettyClient.shutdownGracefully();
    }

}
