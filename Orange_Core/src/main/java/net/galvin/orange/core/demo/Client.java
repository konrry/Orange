package net.galvin.orange.core.demo;

import net.galvin.orange.core.bytecode.ProxyClazzGenerator;
import net.galvin.orange.core.bytecode.impl.ProxyClazzAsmGeneratorImpl;

/**
 * Created by Administrator on 2016/12/7.
 */
public class Client {

    public static void main(String[] args) {
        ProxyClazzGenerator proxyClazzGenerator = ProxyClazzAsmGeneratorImpl.get();
        HelloService helloService = (HelloService) proxyClazzGenerator.generate(HelloService.class);
        for(int i=0;i<10000;i++){
            String hello = helloService.hello("Client"+i);
            System.out.println("hello: "+hello);
        }
    }

}
