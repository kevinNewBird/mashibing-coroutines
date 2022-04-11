package com.mashibing.coroutines.kilim;

import kilim.Pausable;
import kilim.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * description  线程和协程的sleep方法比较测试 <BR>
 * <p>
 * author: zhao.song
 * date: created in 15:22  2022/4/10
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class SleepTest {

    private static Logger log = LogManager.getLogger(SleepTest.class);

    /**
     * 处理器数量
     */
    static int processorNum = Runtime.getRuntime().availableProcessors();

    static {
        log.info("processor number is " + processorNum);
    }

    public static void main(String[] args) throws IOException {
        // （！！！）运行时替换类加载器，非常重要，否则会导致运行不成功
        if (kilim.tools.Kilim.trampoline(true, args)) return;
        if (args.length > 0) {
            createJavaTask();
        } else {
            createKilimTask();
        }
//        System.exit(0);
        System.in.read();

    }

    private static void createKilimTask() {
        for (int i = 0; i < processorNum * 3; i++) {
            new Task() {
                @Override
                public void execute() throws Pausable {
                    Task.sleep(1000);
//                    log.info("[{}] finished", Thread.currentThread().getName());
                    System.out.println(Thread.currentThread().getName()+" finished");
                }
            }.start();
        }
    }

    private static void createJavaTask() {
        for (int i = 0; i < processorNum * 3; i++) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    log.info("[{}] finished", Thread.currentThread().getName());
                    System.out.println(Thread.currentThread().getName()+" finished");
                }

            }.start();
        }
    }
}
