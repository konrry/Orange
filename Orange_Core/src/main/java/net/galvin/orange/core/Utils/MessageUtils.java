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

        //计算消息总长度
        int mogicLength = MogicNumberUtils.getMogicNumberByteLength();
        int length = mogicLength+DATA_LENGTH_FIELD_LENGTH+bodyLength;

        //整个消息的字节数组
        byte[] msgByteArr = new byte[length];

        //拼接魔数部分
        for (int i=0;i<mogicLength;i++){
            msgByteArr[i] = MogicNumberUtils.getMogicNumberBytearr()[i];
        }

        //拼接消息体长度部分
        byte[] bodyLengthArr = SysEnum.long2byte(new Integer(bodyLength).longValue());
        for (int i=mogicLength;i<mogicLength+DATA_LENGTH_FIELD_LENGTH;i++) {
            msgByteArr[i] = bodyLengthArr[i-6];
        }

        //拼接消息体
        for (int i=mogicLength+DATA_LENGTH_FIELD_LENGTH;i<length;i++) {
            msgByteArr[i] = dataByteArr[i-14];
        }
        return msgByteArr;
    }

}
