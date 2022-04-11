package com.mashibing.coroutines.kilim;

import kilim.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * description  kilim框架实现的协程 <BR>
 *
 *     kilim本质是通过asm修改字节码
 * <p>
 * author: zhao.song
 * date: created in 23:10  2022/4/7
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class SimpleKilimCoroutine {
    private static Logger log = LogManager.getLogger(SimpleKilimCoroutine.class);

    public static void main(String[] args) throws IOException {

        // 这里的日志会被输出两次，原因就在于该段代码被kilim字节码层面上代理了
        log.info("start");
        log.error("error");
        // （！！！）运行时替换类加载器，非常重要，否则会导致运行不成功
        if (kilim.tools.Kilim.trampoline(false,args)) return;
        // 常用的协程框架： kilim /quasar /loom
        Task task = new Actor();
        task.run();
        System.out.println("world");
        task.run();
        System.out.println("好");
        task.run();

    }
}
