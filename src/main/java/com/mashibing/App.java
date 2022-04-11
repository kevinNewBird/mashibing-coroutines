package com.mashibing;

import com.mashibing.coroutines.kilim.SimpleKilimCoroutine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger log = LogManager.getLogger(App.class);
    public static void main( String[] args )
    {
        log.info("app start info");
        log.error("app start error");
        System.out.println( "Hello World!" );
    }
}
