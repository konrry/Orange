package net.galvin.orange.core.Utils;


public class SysEnum {

    public static void hello(String userName){
        System.out.println("hello, "+userName);
    }

    /**
     * 格式化异常信息,返回字符串
     */
    public static String format(Throwable e) {
        if(e != null){
            StringBuilder exceptionResult = new StringBuilder(e.toString());
            getFormatString(e, exceptionResult);
            return exceptionResult.toString();
        }
        return null;
    }
    private static void getFormatString(Throwable e,StringBuilder exceptionResult){
        StackTraceElement[] stList = e.getStackTrace();
        if(stList == null) {
            return;
        }
        if(exceptionResult.length() > 0) {
            exceptionResult.append(" Caused by:(");
        }
        for(StackTraceElement ste : stList) {
            exceptionResult.append(ste.getFileName().replace(".java", "."));
            exceptionResult.append(ste.getMethodName());
            exceptionResult.append(":");
            exceptionResult.append(ste.getLineNumber());
            exceptionResult.append(" <- ");
        }
        if(exceptionResult.lastIndexOf(" <- ")==exceptionResult.length()-4) {
            exceptionResult.delete(exceptionResult.length()-4,exceptionResult.length());
        }
        exceptionResult.append(")");

        Throwable e1 = e.getCause();
        if(e1 != null){
            getFormatString(e1, exceptionResult);
        }
    }

    /**
     * 将long型转化成字节数组
     */
    public static byte[] long2byte(long intVal) {
        byte[] targets = new byte[8];
        targets[0] = (byte) (intVal & 0xff);
        targets[1] = (byte) ((intVal >> 8) & 0xff);
        targets[2] = (byte) ((intVal >> 16) & 0xff);
        targets[3] = (byte) ((intVal >> 24) & 0xff);
        targets[4] = (byte) ((intVal >> 32) & 0xff);
        targets[5] = (byte) ((intVal >> 40) & 0xff);
        targets[6] = (byte) ((intVal >> 48) & 0xff);
        targets[7] = (byte) (intVal >>> 56);
        return targets;
    }

    /**
     * 字节数组转换成long
     */
    public static Long byte2long(byte[] byteArr){
        if(byteArr == null || byteArr.length != 8){
            return  null;
        }
        long longVal = (long) ((byteArr[0] & 0xFF)
                        | ((byteArr[1] & 0xFF) << 8)
                        | ((byteArr[2] & 0xFF) << 16)
                        | ((byteArr[3] & 0xFF) << 24)
                        | ((byteArr[4] & 0xFF) << 32)
                        | ((byteArr[5] & 0xFF) << 40)
                        | ((byteArr[6] & 0xFF) << 48)
                        | ((byteArr[7] & 0xFF) << 56));
        return longVal;
    }

}
