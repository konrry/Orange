package net.galvin.orange.core.transport.comm;

import net.galvin.orange.core.transport.netty.NettySessionManager;

/**
 * Created by Administrator on 2016/12/7.
 */
public class NetTransportProxy {

    private static NetTransportProxy netTransportProxy = null;

    private NetTransportProxy(){}

    public static NetTransportProxy get(){
        if(netTransportProxy == null){
            synchronized (NetTransportProxy.class){
                if(netTransportProxy == null){
                    netTransportProxy = new NetTransportProxy();
                }
            }
        }
        return netTransportProxy;
    }

    public String send(String msg) {
        return NettySessionManager.get().session(null,null).writeAndFlush(msg);
    }




}
