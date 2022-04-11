package com.mashibing.coroutines.kilim;

import kilim.Pausable;
import kilim.Task;

/**
 * description  大量任务创建（线程和协程比较） <BR>
 * <p>
 * author: zhao.song
 * date: created in 16:39  2022/4/8
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class LotOfJavaTask {
    static int n = 10_000;

    public static void main(String[] args) {
        // （！！！）运行时替换类加载器，非常重要，否则会导致运行不成功
        if (kilim.tools.Kilim.trampoline(false, args)) return;
        if (args.length > 0) {
            createJavaTask();
        } else {
            createKilimTask();
        }
        System.exit(0);
    }

    public static void createJavaTask() {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            Thread t = new Thread();
            t.start();
            if (i % n == 0) {
                System.out.println(" created " + i + " common tasks cost " + (System.nanoTime() - start));
            }
        }
    }

    public static void createKilimTask() {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            Task t = new ATask();
            t.start();
            if (i % n == 0) {
                System.out.println(" created " + i + "  tasks ... (contd.) cost " + (System.nanoTime() - start));
            }
        }
    }

    static class ATask extends Task {

        @Override
        public void execute() throws Pausable, Exception {

        }
    }
}
