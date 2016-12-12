package net.galvin.orange.core.comm.impl;

import net.galvin.orange.core.comm.NetTransportClient;
import net.galvin.orange.core.transport.netty.NettySessionManager;

/**
 * Created by Administrator on 2016/12/7.
 */
public class NetTransportClientImpl implements NetTransportClient {

    private static NetTransportClient netTransportClient = null;

    private NetTransportClientImpl(){}

    public static NetTransportClient get(){
        if(netTransportClient == null){
            synchronized (NetTransportClientImpl.class){
                if(netTransportClient == null){
                    netTransportClient = new NetTransportClientImpl();
                }
            }
        }
        return netTransportClient;
    }

    @Override
    public void send(String msg) {
        NettySessionManager.get().session(null,null).writeAndFlush(msg.getBytes());
    }




}
