package net.galvin.orange.core.transport.netty;

import java.util.Date;

public class NettySession {

    private String remoteAddress;
    private Integer remotePort;
    private final Date createTime;

    private NettyTransportClient nettyTransportClient;

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

    public NettyTransportClient getNettyTransportClient() {
        return nettyTransportClient;
    }

    public void setNettyTransportClient(NettyTransportClient nettyTransportClient) {
        this.nettyTransportClient = nettyTransportClient;
    }

    public void writeAndFlush(String msg){
        if(this.nettyTransportClient == null){
            synchronized (this){
                if(this.nettyTransportClient == null){
                    NettyTransportClient.newInstance(this);
                }
            }
        }
        this.nettyTransportClient.writeAndFlush(msg);
    }

    public void shutdownGracefully(){
        this.nettyTransportClient.shutdownGracefully();
    }

}
