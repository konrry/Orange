package net.galvin.orange.core.transport.netty.client;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NettyClientSessionManager {

    private static NettyClientSessionManager sessionManager = null;
    private final Map<String,NettyClientSession> nettySessionMap;
    private final Lock lock = new ReentrantLock();

    private NettyClientSessionManager(){
        this.nettySessionMap = new ConcurrentHashMap<String,NettyClientSession>();
    }

    public static NettyClientSessionManager get(){
        if(sessionManager == null){
            synchronized (NettyClientSessionManager.class){
                if(sessionManager == null){
                    sessionManager = new NettyClientSessionManager();
                }
            }
        }
        return sessionManager;
    }

    public NettyClientSession session(String remoteAddress, Integer port){
        if(StringUtils.isEmpty(remoteAddress)){
            remoteAddress = "127.0.0.1";
        }
        if(port == null || port <= 1000){
            port = 9028;
        }
        String key = remoteAddress+":"+port;
        if(this.nettySessionMap.get(key) == null){
            lock.lock();
            if(this.nettySessionMap.get(key) == null){
                this.nettySessionMap.put(key,new NettyClientSession(remoteAddress,port));
            }
            lock.unlock();
        }
        return this.nettySessionMap.get(key);
    }

}
