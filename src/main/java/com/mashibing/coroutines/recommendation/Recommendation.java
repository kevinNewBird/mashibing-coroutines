package com.mashibing.coroutines.recommendation;

import java.util.Map;

/**
 * description  Recommendation <BR>
 * <p>
 * author: zhao.song
 * date: created in 18:15  2022/4/8
 * company: TRS信息技术有限公司
 * version 1.0
 */
public interface Recommendation {

    Map<String, String> getRecommendedVideos(String[] accountIds);
}
