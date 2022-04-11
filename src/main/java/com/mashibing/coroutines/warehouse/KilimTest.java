package com.mashibing.coroutines.warehouse;

import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;
import kilim.tools.Kilim;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * description  kilim框架实现 生产者/消费者 <BR>
 * <p>
 * author: zhao.song
 * date: created in 14:29  2022/4/11
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class KilimTest {

    static Map<Integer, Mailbox<Integer>> mailboxMap = new HashMap<>();

    public static void main(String[] args) {
        // 1.判断是否引入了kilim
        if (Kilim.trampoline(false, args)) return;

        // 2.配置
        Properties kilimProps = new Properties();
        kilimProps.setProperty("kilim.Scheduler.numThreads", "4");//设置四个工作线程
        System.setProperties(kilimProps);

        long start = System.currentTimeMillis();
        int produceWorkNum = 1000;
        int consumeWorkNum = 1000;

        // 3.生产
        for (int taskId = 0; taskId < produceWorkNum; taskId++) {
            // 仓库，最大容量10
            Mailbox<Integer> warehouse = new Mailbox<>(1, 10);
            new ProducerTask(taskId, warehouse).start();
            mailboxMap.put(taskId, warehouse);
        }

        // 4.消费
        for (int taskId = 0; taskId < consumeWorkNum; taskId++) {
            new ConsumerTask(mailboxMap.get(taskId)).start();
        }

        Task.idledown();//开始运行

        long end = System.currentTimeMillis();
        System.out.println("协程执行时长：" + (end - start) + " ms");
    }


    static class ProducerTask extends Task {
        static AtomicLong producerCount = new AtomicLong(0);
        int count = 0;
        Mailbox<Integer> warehouse;

        private static final int WORK_CAPACITY = 10;

        public ProducerTask(int taskId, Mailbox<Integer> warehouse) {
            this.count = taskId;
            this.warehouse = warehouse;
        }

        @Override
        public void execute() throws Pausable {
            for (int i = 0; i < WORK_CAPACITY; i++) {
                warehouse.put(count++);
                long produceNum = producerCount.incrementAndGet();
                System.out.println(Thread.currentThread().getName() + "生产者，目前生产数量：" + produceNum);
            }
        }
    }

    static class ConsumerTask extends Task {

        static AtomicLong consumerCount = new AtomicLong(0);

        Mailbox<Integer> warehouse;

        private static final int WORK_CAPACITY = 10;

        public ConsumerTask(Mailbox<Integer> warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public void execute() throws Pausable {
            for (int i = 0; i < WORK_CAPACITY; i++) {
                // 获取消息，阻塞协程线程（在mailbox为空是，消费者get会被阻塞）
                Integer goodsId = warehouse.get();
                long consumeNum = consumerCount.incrementAndGet();
                System.out.println(Thread.currentThread().getName() + "消费者，目前消费数量：" + consumeNum);
            }
        }
    }

}
