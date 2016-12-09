package net.galvin.orange.core.demo;

public class HelloServiceImpl implements HelloService {

    private String defaultName = " Galvin";

    public String hello(String userName) {
        if(userName == null || userName.trim().equals("")){
            userName = defaultName;
        }
        return "Hello, "+userName;
    }
}
