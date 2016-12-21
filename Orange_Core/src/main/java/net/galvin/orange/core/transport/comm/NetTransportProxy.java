package net.galvin.orange.core.transport.comm;

import net.galvin.orange.core.Utils.JDKSerializeUtils;
import net.galvin.orange.core.Utils.MessageUtils;
import net.galvin.orange.core.Utils.MogicNumberUtils;
import net.galvin.orange.core.Utils.SysEnum;
import net.galvin.orange.core.transport.netty.client.NettyClientSession;
import net.galvin.orange.core.transport.netty.client.NettyClientSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NetTransportProxy {

    private final Logger logger = LoggerFactory.getLogger(NetTransportProxy.class);

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
        NetRequest<String> netRequest = new NetRequest<String>(msg);
        byte[] byteArr = JDKSerializeUtils.serialize(netRequest);
        byte[] msgByteArr = MessageUtils.buildNetMessage(byteArr);
        NettyClientSession nettyClientSession = NettyClientSessionManager.get().session(null,null);
        nettyClientSession.connect();
        if(msgByteArr != null && msgByteArr.length > 0){
            NetTransportManager.createLock(netRequest.getRequestId());
            nettyClientSession.writeAndFlush(msgByteArr);
        }
        NetResponse<String> netResponse = NetTransportManager.read(netRequest.getRequestId());
        return netResponse.getBody();
    }

}
