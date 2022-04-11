package com.mashibing.coroutines.warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * description  生产者/消费者模型（有限的仓库容量） <BR>
 * <p>
 * author: zhao.song
 * date: created in 14:03  2022/4/11
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class MultiThreadTest {

    /**
     * 仓库，容量为10
     */
    private static final LinkedBlockingQueue<Integer> warehouse = new LinkedBlockingQueue<>(10);

    /**
     * 生产者--生产数量
     */
    private static AtomicLong producerCount = new AtomicLong(0);

    /**
     * 消费者--消费数量
     */
    private static AtomicLong consumerCount = new AtomicLong(0);

    public static void main(String[] args) {
        int produceWorkNum = 1000;
        int consumeWorkNum = 1000;

        long start = System.currentTimeMillis();
        List<Thread> workers = new ArrayList<>();
        // 1.生产
        for (int i = 0; i < produceWorkNum; i++) {
            Thread produceWorker = new Thread(new Producer());
            produceWorker.start();
            workers.add(produceWorker);
        }
        // 2.消费
        for (int i = 0; i < consumeWorkNum; i++) {
            Thread consumeWorker = new Thread(new Consumer());
            consumeWorker.start();
            workers.add(consumeWorker);
        }

        // 3.确保任务执行完成
        workers.forEach(worker -> {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        long end = System.currentTimeMillis();
        System.out.println("多线程执行时长：" + (end - start) + " ms");
    }

    /**
     * 生产者（生产10个商品）
     */
    static class Producer implements Runnable {

        private static final int WORK_CAPACITY = 10;

        @Override
        public void run() {
            for (int i = 0; i < WORK_CAPACITY; i++) {
                try {
                    warehouse.put(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long produceNum = producerCount.incrementAndGet();
                System.out.println(Thread.currentThread().getName() + "生产者，目前生产数量：" + produceNum);
            }
        }
    }

    /**
     * 消费者（消费10个商品）
     */
    static class Consumer implements Runnable {

        private static final int WORK_CAPACITY = 10;

        @Override
        public void run() {
            for (int i = 0; i < WORK_CAPACITY; i++) {
                try {
                    warehouse.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long consumeNum = consumerCount.incrementAndGet();
                System.out.println(Thread.currentThread().getName() + "消费者，目前消费数量：" + consumeNum);
            }
        }
    }
}
