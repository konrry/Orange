package net.galvin.orange.core.transport.comm;

import java.io.Serializable;
import java.util.UUID;

public class NetRequest<T> implements Serializable {

    private static final long serialVersionUID = 2161875797958655750L;
    private final String requestId;
    private final T body;

    public NetRequest(T body){
        this.requestId = UUID.randomUUID().toString();
        this.body = body;
    }

    public String getRequestId() {
        return requestId;
    }

    public T getBody() {
        return body;
    }


    @Override
    public String toString() {
        return "requestId: "+requestId;
    }

}
