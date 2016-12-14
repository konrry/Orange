package net.galvin.orange.core.demo;

import net.galvin.orange.core.comm.ProxyClazzGenerator;
import net.galvin.orange.core.comm.impl.ProxyClazzAsmGeneratorImpl;

/**
 * Created by Administrator on 2016/12/7.
 */
public class Client {

    public static void main(String[] args) {

        ProxyClazzGenerator proxyClazzGenerator = ProxyClazzAsmGeneratorImpl.get();
        System.out.println("子类生成 start");
        HelloService helloService = (HelloService) proxyClazzGenerator.generate(HelloService.class);
        System.out.println("子类生成 end");
        System.out.println("调用子类方法 hello start");
        String hello = helloService.hello("Galvin_HelloService");
        System.out.println("调用子类方法 hello end");
    }

}
