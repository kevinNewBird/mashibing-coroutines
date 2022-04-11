package com.mashibing.coroutines.recommendation;

import kilim.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/**
 * description  协程的方式
 * <br/>
 * <br/>
 * <br/>
 *  解释：
 *  mailBox.get()，并不会阻塞主线程，相当于yeild放弃了主线程的持有
 *  ，释放自身所持有的资源，交由其他任务的线程进行处理，处理完成后结果
 *  放入mailBox,监听器监听到事件后this.run方法继续向下执行<BR>
 * <p>
 * author: zhao.song
 * date: created in 18:23  2022/4/8
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class PausableRecommendation implements Recommendation {

    private static Logger log = LogManager.getLogger(Recommendation.class);

    public static void main(String[] args) throws IOException {
//        if (kilim.tools.Kilim.trampoline(false,args)) return;
        PausableRecommendation recommendation = new PausableRecommendation();
        long start = System.nanoTime();
        log.info(recommendation.getRecommendedVideos(new String[]{"1", "2", "3", "4", "5"})
                + " task cost:" + (System.nanoTime() - start));
//        System.exit(0);
        System.in.read();
    }

    @Override
    public Map<String, String> getRecommendedVideos(String[] accountIds) {
        Map<String, String> result = new HashMap<>();
        ArrayDeque<Recommended> ad = new ArrayDeque<>();
        for (String accountId : accountIds) {
            Recommended task = new Recommended(accountId);
            task.run();
            ad.push(task);
        }
        while (!ad.isEmpty()) {
            Recommended item = ad.poll();
            ExitMsg<String> msg = item.joinb();
            result.put(item.getAccountId(), msg.result);
        }
        return result;
    }

    static class Recommended extends Task.Spawn<String> {

        private Mailbox<Map<String, Double>> tagBox = new Mailbox<Map<String, Double>>();
        private Mailbox<Map<String, Map<String, Double>>> factorBox = new Mailbox<Map<String, Map<String, Double>>>();
        private String accountId;

        public Recommended(String accountId) {
            this.accountId = accountId;
            // 当监听器发现mailBox有数据后，执行continueRun->this.run,从而唤醒线程yield继续往下执行
            factorBox.addMsgAvailableListener(new EventSubscriber() {
                @Override
                public void onEvent(EventPublisher eventPublisher, Event event) {
                    continueRun();
                }
            });
        }


        public String getAccountId() {
            return this.accountId;
        }

        public void continueRun() {
            log.info("[{}] continue run",accountId);
            this.run();
        }

        @Override
        public void execute() throws Pausable, Exception {
            // 执行任务(耗时比较长)
            new TagTask(accountId, tagBox).start();
            new FactorTask(accountId, factorBox).start();

            log.info("[{}] rest for a while",accountId);
            // 因为知道前面两个任务耗时长，所以释放掉不等待
            this.yield();
            log.info("[{}] recommendation task begin",accountId);
            // 当再次被唤起时，直接从结果中去获取（也就是监听器MsgAvailableListener）
            exitResult = Utils.getRecommendedVideos(tagBox.get(), factorBox.get());
        }

    }

    /**
     * 用户历史观看记录标签权重 任务类(Spawn和Task唯一的区别是一个有返回结果，一个没有)
     */
    static class TagTask extends Task.Spawn<Map<String, Double>> {
        private Mailbox<Map<String, Double>> mailbox;
        private String accountId;

        public TagTask(String accountId, Mailbox<Map<String, Double>> mailbox) {
            this.mailbox = mailbox;
            this.accountId = accountId;
        }

        @Override
        public void execute() throws Pausable, Exception {
            log.info("[{}] coroutine tag task begin", accountId);
            long start = System.nanoTime();
            mailbox.put(Utils.getViewHistoryTag(accountId));
            log.info("[{}] coroutine tag task cost:" + (System.nanoTime() - start), accountId);
        }
    }

    /**
     * 时下最热电影 任务类
     */
    static class FactorTask extends Task {

        // 存放运算的结果
        private Mailbox<Map<String, Map<String, Double>>> mailbox;

        private String accountId;

        public FactorTask(String accountId, Mailbox<Map<String, Map<String, Double>>> mailbox) {
            this.mailbox = mailbox;
            this.accountId = accountId;
        }

        @Override
        public void execute() throws Pausable, Exception {
            log.info("[{}] coroutine factor task begin", accountId);
            long start = System.nanoTime();
            mailbox.put(Utils.getHottestMovie());
            log.info("[{}] coroutine factor task cost:" + (System.nanoTime() - start), accountId);
        }
    }
}
