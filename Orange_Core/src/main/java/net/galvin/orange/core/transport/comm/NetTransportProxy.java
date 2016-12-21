package net.galvin.orange.core.transport.comm;

import net.galvin.orange.core.Utils.JDKSerializeUtils;
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
        if(byteArr == null || byteArr.length <= 0){
            logger.error("The byteArr is empty ... ");
            return null;
        }
        int bodyLength = byteArr.length;
        int length = 6 + 8 + bodyLength;
        byte[] msgByteArr = new byte[length];
        for (int i=0;i<6;i++){
            msgByteArr[i] = SysEnum.MOGIC_BYTE_ARRAY[i];
        }
        byte[] bodyLengthArr = SysEnum.long2byte(new Integer(bodyLength).longValue());
        for (int i=6;i<14;i++) {
            msgByteArr[i] = bodyLengthArr[i-6];
        }
        for (int i=14;i<length;i++) {
            msgByteArr[i] = byteArr[i-14];
        }
        NettyClientSession nettyClientSession = NettyClientSessionManager.get().session(null,null);
        nettyClientSession.connect();
        nettyClientSession.writeAndFlush(msgByteArr);
        return nettyClientSession.read();
    }

}
