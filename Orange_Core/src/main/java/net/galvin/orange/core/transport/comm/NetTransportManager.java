package net.galvin.orange.core.transport.comm;

import net.galvin.orange.core.Utils.SysEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 网络传输管理器
 */
public class NetTransportManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(NetTransportManager.class);

    private final static Map<String,Lock> requestReadLockMap = new ConcurrentHashMap<String,Lock>();
    private final static Map<String,NetResponse> netResponseMap = new ConcurrentHashMap<String,NetResponse>();

    //默认500毫秒
    private static long timeOut = 50000;

    /**
     * 创建lock
     */
    public static void createLock(String requestId){
        if(StringUtils.isEmpty(requestId)){
            return;
        }
        Lock lock = new ReentrantLock();
        requestReadLockMap.put(requestId,lock);
    }

    /**
     * 客户端调用该方法可以获取服务端返回值
     */
    public static NetResponse read(String requestId){
        if(StringUtils.isEmpty(requestId)){
            return null;
        }
        NetResponse response = netResponseMap.get(requestId);
        if(response == null){
            Lock lock = requestReadLockMap.get(requestId);
            synchronized (lock){
                response = netResponseMap.get(requestId);
                if(response == null){
                    try {
                        lock.wait();
                        response = netResponseMap.get(requestId);
                    } catch (Exception e) {
                        LOGGER.error(SysEnum.format(e));
                    }
                }
            }
            if(response == null){
                LOGGER.error("Orange's client time out!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }
        requestReadLockMap.remove(requestId);
        netResponseMap.remove(requestId);
        return response;
    }

    /**
     * 客户端处理器,调用该方法,通知获取返回值
     */
    public static void notify(String requestId, NetResponse netResponse){
        if(StringUtils.isEmpty(requestId)){
            return;
        }
        netResponseMap.put(requestId,netResponse);
        Lock lock = requestReadLockMap.get(requestId);
        synchronized (lock){
            lock.notifyAll();
        }
    }


}
