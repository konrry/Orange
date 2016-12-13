package net.galvin.orange.core.demo;

public class HelloServiceImpl implements HelloService {


    private static HelloService helloService;

    public static HelloService get(){
        if(helloService == null){
            synchronized (HelloServiceImpl.class){
                if(helloService == null){
                    helloService = new HelloServiceImpl();
                }
            }
        }
        return helloService;
    }


    private String defaultName = " Galvin";

    public String hello(String userName) {
        if(userName == null || userName.trim().equals("")){
            userName = defaultName;
        }
        return "Hello, this is "+userName;
    }
}
