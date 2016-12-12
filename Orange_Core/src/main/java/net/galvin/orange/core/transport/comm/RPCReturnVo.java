package net.galvin.orange.core.transport.comm;

/**
 * 远程调用返回值调用
 */
public class RPCReturnVo {

    private final STATUS status;
    private Byte[] returnVal;
    private String comment;

    private RPCReturnVo(STATUS status) {
        this.status = status;
    }

    /**
     * 远程调用状态
     */
    private enum STATUS {
        SUCCESS,FAILED;
    }

    //调用是否成功
    public boolean isSuccess(){
        return STATUS.SUCCESS.equals(status) ? true : false;
    }
    public boolean isFailed(){
        return !this.isSuccess();
    }

    public static RPCReturnVo buildErrorVo(){
        RPCReturnVo rpcReturnVo = new RPCReturnVo(STATUS.FAILED);
        return rpcReturnVo;
    }

    public static RPCReturnVo buildSuccessVo(){
        RPCReturnVo rpcReturnVo = new RPCReturnVo(STATUS.SUCCESS);
        return rpcReturnVo;
    }

}
