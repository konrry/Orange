package net.galvin.orange.core.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Jdk自带的序列化工具
 */
public class JDKSerializeUtils {

    private static final Logger logger = LoggerFactory.getLogger(JDKSerializeUtils.class);

    public static byte[] serialize(Object object) {
        if(object==null) {
            logger.info("The object is null");
            return null;
        }
        byte[] byteArr = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            byteArr = byteArrayOutputStream.toByteArray();
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            if(objectOutputStream == null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if(byteArrayOutputStream == null){
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return byteArr;
    }

    public static Object deserialize(byte[] byteArr) {
        if(byteArr==null || byteArr.length <= 0) {
            logger.info("The object | byteArr is null");
            return null;
        }
        Object object = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(byteArr);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            object = objectInputStream.readObject();
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            if(objectInputStream == null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if(byteArrayInputStream == null){
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return object;
    }

}
