package com.mashibing.coroutines.recommendation;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * description  多线程的方式
 *  * <br/>
 *  * <br/>
 *  * <br/>
 *  解释：
 *  task.get(),阻塞主线程
 *  <BR>
 * <p>
 * author: zhao.song
 * date: created in 18:23  2022/4/8
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class MultiThreadRecommendation implements Recommendation {

    private static Logger log = LogManager.getLogger(MultiThreadRecommendation.class.getName());
    ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public Map<String, String> getRecommendedVideos(String[] accountIds) {
        Map<String, String> result = new HashMap<>();
        for (String accountId : accountIds) {
            result.put(accountId, getRecommendedVideos(accountId));
        }
        return result;
    }

    public String getRecommendedVideos(String accountId) {
        // 1.任务一：获取最热电影
        Callable<Map<String, Map<String, Double>>> hotMovie = Utils::getHottestMovie;
        FutureTask<Map<String, Map<String, Double>>> task1 = new FutureTask<Map<String, Map<String, Double>>>(hotMovie);
        log.info("submit task for hot movie data");
        executor.submit(task1);

        // 2.任务二：获取历史电影的标签权重
        Callable< Map<String, Double>> history = () -> Utils.getViewHistoryTag(accountId);
        FutureTask<Map<String, Double>> task2 = new FutureTask<Map<String, Double>>(history);
        log.info("submit task for hot view history data");
        executor.submit(task2);

        // 3.获取满足的推荐电影
        try {
            log.info("retrieve the best recommendation movie for user...");
            return Utils.getRecommendedVideos(task2.get(),task1.get());
        } catch (InterruptedException|ExecutionException e) {
            log.error("retrieving the best recommendation movie occur error!");
        }
        return null;
    }
}
