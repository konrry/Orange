package net.galvin.orange.core.transport.comm;

import io.netty.buffer.ByteBuf;
import net.galvin.orange.core.Utils.JDKSerializeUtils;
import net.galvin.orange.core.Utils.MogicNumberUtils;
import net.galvin.orange.core.Utils.SysEnum;

import java.util.ArrayList;
import java.util.List;

public class MessageCodec<T> {

    private STATUS status = STATUS.READY;
    private byte[] mogicByteArr = new byte[6];
    private int mogicIndex = -1;
    private byte[] dataLengthByteArr = new byte[8];
    private int dataLengthIndex = -1;
    private byte[] dataByteArr;
    private int dataIndex = -1;

    private T t;



    private List<Byte> byteList = new ArrayList<Byte>();

    /**
     * 编码解码器
     */
    public boolean codec(ByteBuf byteBuf){
        if(byteBuf == null) return false;
        if(!byteBuf.isReadable()) return false;
        if(STATUS.READY.name().equals(status.name())) this.status = STATUS.MOGIC;

        //读取魔数
        while(byteBuf.isReadable() && STATUS.MOGIC.name().equals(status.name())){
            mogicByteArr[++mogicIndex] = byteBuf.readByte();
            if(mogicIndex >= 5){
                if(MogicNumberUtils.isMogicNumber(mogicByteArr)){
                    this.status = STATUS.DATA_LENGTH;
                }else {
                    this.status = STATUS.READY;
                }
            }
        }

        //读取数据长度
        while(byteBuf.isReadable() && STATUS.DATA_LENGTH.name().equals(status.name())){
            dataLengthByteArr[++dataLengthIndex] = byteBuf.readByte();
            if(dataLengthIndex >= 7) {
                this.status = STATUS.DATA;
                long dataLength = SysEnum.byte2long(dataLengthByteArr);
                dataByteArr = new byte[new Long(dataLength).intValue()];
            }
        }

        //读取请求,或者响应
        while (byteBuf.isReadable() && STATUS.DATA.name().equals(status.name())){
            dataByteArr[++dataIndex] = byteBuf.readByte();
            if(dataIndex >= dataByteArr.length-1){
                this.status = STATUS.SUCCESS;
            }
        }

        if(STATUS.SUCCESS.name().equals(status.name())){
            this.t = (T) JDKSerializeUtils.deserialize(dataByteArr);
            return true;
        }
        return false;
    }

    /**
     * 重置状态
     */
    public void reset(){
        this.status = STATUS.READY;
        this.mogicByteArr = new byte[6];
        this.mogicIndex = -1;
        this.dataLengthByteArr = new byte[8];
        this.dataLengthIndex = -1;
        this.dataByteArr = null;
        this.dataIndex = -1;
        this.t = null;
    }

    /**
     * 数据接收状态
     */
    private enum STATUS {
        READY("准备就绪"),
        MOGIC("接收魔数"),
        DATA_LENGTH("接收数据长度"),
        DATA("接收数据"),
        SUCCESS("数据接收成功");
        private String cName;
        STATUS(String cName) {
            this.cName = cName;
        }
    }


    public T getT() {
        return t;
    }
}
