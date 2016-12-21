package net.galvin.orange.core.transport.netty.client;

import java.util.Date;
import java.util.UUID;

public class NettyClientSession {

    private String seesionId;
    private String remoteAddress;
    private Integer remotePort;
    private final Date createTime;

    private NettyClient nettyClient;

    public NettyClientSession(String remoteAddress, Integer remotePort) {
        this.seesionId = UUID.randomUUID().toString();
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

    public String getSeesionId() {
        return seesionId;
    }

    public void connect(){
        if(this.nettyClient == null){
            synchronized (this){
                if(this.nettyClient == null){
                    NettyClient.newInstance(this);
                }
            }
        }
    }

    public void writeAndFlush(byte[] msg){
        this.nettyClient.writeAndFlush(msg);
    }

    public String read(){
        return this.nettyClient.read();
    }

    public void shutdownGracefully(){
        this.nettyClient.shutdownGracefully();
    }

}
