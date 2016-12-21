package net.galvin.orange.core.Utils;

/**
 * 消息协议
 */
public class MessageUtils {

    private final static int DATA_LENGTH_FIELD_LENGTH = 8;

    /**
     * 生成消息
     */
    public static byte[] buildNetMessage(byte[] dataByteArr){
        if(dataByteArr == null || dataByteArr.length <= 0){
            return null;
        }
        int bodyLength = dataByteArr.length;
        int length = MogicNumberUtils.getMogicNumberByteLength()+DATA_LENGTH_FIELD_LENGTH+bodyLength;
        byte[] msgByteArr = new byte[length];
        for (int i=0;i<6;i++){
            msgByteArr[i] = MogicNumberUtils.getMogicNumberBytearr()[i];
        }
        byte[] bodyLengthArr = SysEnum.long2byte(new Integer(bodyLength).longValue());
        for (int i=6;i<14;i++) {
            msgByteArr[i] = bodyLengthArr[i-6];
        }
        for (int i=14;i<length;i++) {
            msgByteArr[i] = dataByteArr[i-14];
        }
        return null;
    }

}
