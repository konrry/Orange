package net.galvin.orange.core.Utils;

/**
 * 魔法数
 */
public class MogicNumberUtils {

    private final static String MOGIC_NUMBER = "orange";
    private final static byte[] MOGIC_NUMBER_BYTEARR;
    private final static int MOGIC_NUMBER_BYTE_LENGTH;

    static {
        MOGIC_NUMBER_BYTEARR = MOGIC_NUMBER.getBytes();
        MOGIC_NUMBER_BYTE_LENGTH = MOGIC_NUMBER_BYTEARR.length;
    }

    public static byte[] getMogicNumberBytearr() {
        return MOGIC_NUMBER_BYTEARR;
    }

    public static int getMogicNumberByteLength() {
        return MOGIC_NUMBER_BYTE_LENGTH;
    }

    /** 判断是否是魔数 */
    public static boolean isMogicNumber(String mogicNumber){
        return MOGIC_NUMBER.equals(mogicNumber);
    }
    public static boolean isMogicNumber(byte[] mogicNumberByteArr){
        return (mogicNumberByteArr != null) && MOGIC_NUMBER.equals(new String(mogicNumberByteArr));
    }

}
