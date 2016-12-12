package net.galvin.orange.core.transport.netty;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by qchu on 16-12-11.
 */
public class NettySessionManager {

    private static NettySessionManager sessionManager = null;
    private final Map<String,NettySession> nettySessionMap;
    private final Lock lock = new ReentrantLock();

    private NettySessionManager(){
        this.nettySessionMap = new ConcurrentHashMap<String,NettySession>();
    }

    public static NettySessionManager get(){
        if(sessionManager == null){
            synchronized (NettySessionManager.class){
                if(sessionManager == null){
                    sessionManager = new NettySessionManager();
                }
            }
        }
        return sessionManager;
    }

    public NettySession session(String remoteAddress,Integer port){
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
                this.nettySessionMap.put(key,new NettySession(remoteAddress,port));
            }
            lock.unlock();
        }
        return this.nettySessionMap.get(key);
    }



}
