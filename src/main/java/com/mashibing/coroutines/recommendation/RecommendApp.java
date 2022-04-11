package com.mashibing.coroutines.recommendation;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


/**
 * description  RecommendApp <BR>
 * <p>
 * author: zhao.song
 * date: created in 18:09  2022/4/8
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class RecommendApp {

    private static Logger log = LogManager.getLogger(RecommendApp.class);

    /**
     * mvn package exec:java -Dexec.mainClass=com.mashibing.coroutines.recommendation.RecommendApp -Dexec.args=1
     * @param args
     */
    public static void main(String[] args) throws IOException {
        log.error("ssssss");
        if (kilim.tools.Kilim.trampoline(false,args)) return;
        Recommendation recommend = null;
        if (args == null || args.length == 0) {
            recommend = new MultiThreadRecommendation();
        }else{
            recommend = new PausableRecommendation();
        }

        long start = System.nanoTime();
        String[] accountIds = new String[]{"1"};
        log.info(recommend.getRecommendedVideos(accountIds).toString());
        log.info("total task cost:" + (System.nanoTime() - start));
        System.exit(0);
    }
}
