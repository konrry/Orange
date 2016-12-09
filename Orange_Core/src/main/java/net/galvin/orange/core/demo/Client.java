package net.galvin.orange.core.demo;

import net.galvin.orange.core.comm.ProxyClazzGenerator;
import net.galvin.orange.core.comm.impl.ProxyClazzAsmGeneratorImpl;

/**
 * Created by Administrator on 2016/12/7.
 */
public class Client {

    public static void main(String[] args) {

        ProxyClazzGenerator proxyClazzGenerator = ProxyClazzAsmGeneratorImpl.get();
        HelloService helloService = (HelloService) proxyClazzGenerator.generate(HelloService.class);
        String hello = helloService.hello("Galvin");
        System.out.println(hello);
    }

}
