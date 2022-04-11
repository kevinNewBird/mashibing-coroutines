package com.mashibing.coroutines.recommendation;

import java.util.HashMap;
import java.util.Map;

/**
 * description  Utils <BR>
 * <p>
 * author: zhao.song
 * date: created in 18:28  2022/4/8
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class Utils {


    /**
     * description   通过用户历史标签，从时下最热电影中获取推荐电影  <BR>
     *
      * @param userTag:
     * @param movieFactor:
     * @return {@link String}
     * @author zhao.song  2022/4/9  10:53
     */
    public static String getRecommendedVideos(Map<String, Double> userTag
            , Map<String, Map<String, Double>> movieFactor) {
        double maxScore = 0;
        String result = null;
        for (String movie : movieFactor.keySet()) {
            Map<String, Double> factor = movieFactor.get(movie);
            double score = 0;
            for (String tag : userTag.keySet()) {
                if (factor.containsKey(tag)) {
                    score += factor.get(tag) * userTag.get(tag);
                }
            }
            if (score > maxScore) {
                maxScore = score;
                result = movie;
            }
        }
        return result;
    }

    /**
     * description   获取时下最热视频  <BR>
     *
      * @param : 
     * @return {@link Map< String, Map< String, Double>>}
     * @author zhao.song  2022/4/9  10:53
     */
    public static Map<String, Map<String, Double>> getHottestMovie() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Double> lj = new HashMap<String, Double>();
        lj.put("战争", 0.9);
        lj.put("爱情", 0.8);

        Map<String, Double> zl = new HashMap<String, Double>();
        zl.put("战争", 0.7);
        zl.put("爱情", 0.6);
        zl.put("爱国", 0.8);

        Map<String, Map<String, Double>> movieFactor = new HashMap<>();
        movieFactor.put("乱世佳人", lj);
        movieFactor.put("战狼", zl);

        return movieFactor;
    }

    /**
     * description   通过历史记录获取用户感兴趣的标签  <BR>
     *
      * @param accountId: 
     * @return {@link Map< String, Double>}
     * @author zhao.song  2022/4/9  10:53
     */
    public static Map<String, Double> getViewHistoryTag(String accountId) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Double> result = new HashMap<String, Double>();
        result.put("战争", 1.0);
        result.put("爱情", 0.8);
        return result;
    }
}
