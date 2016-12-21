package net.galvin.orange.core.transport.comm;

import java.io.Serializable;

public class NetResponse<T> implements Serializable {

    private static final long serialVersionUID = -5407952809054429948L;

    private final String requestId;
    private final T body;

    public NetResponse(String requestId, T body) {
        this.requestId = requestId;
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    public String getRequestId() {
        return requestId;
    }
}
