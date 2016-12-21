package net.galvin.orange.core.demo;

import net.galvin.orange.core.Utils.JDKSerializeUtils;
import net.galvin.orange.core.Utils.SysEnum;
import net.galvin.orange.core.transport.comm.NetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qchu on 16-12-11.
 */
public class Demo {

    private static Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        String s = "abcd";
        System.out.println(s.getBytes().length);
    }


}
