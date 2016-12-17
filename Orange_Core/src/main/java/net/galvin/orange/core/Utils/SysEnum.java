package net.galvin.orange.core.Utils;

/**
 * Created by Administrator on 2016/12/7.
 */
public class SysEnum {

    public static void hello(String userName){
        System.out.println("hello, "+userName);
    }

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

}
