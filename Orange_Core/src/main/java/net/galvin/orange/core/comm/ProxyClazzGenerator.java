package net.galvin.orange.core.comm;

/**
 * 字节码生成器接口
 */
public interface ProxyClazzGenerator {

    Object generate(Class clazz);

}
