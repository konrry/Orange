package net.galvin.orange.core.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qchu on 16-12-11.
 */
public class Demo {

    private static Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {

        logger.warn("log4j warn level");
        logger.info("log4j info level");
        logger.debug("log4j debug level");


    }


}
