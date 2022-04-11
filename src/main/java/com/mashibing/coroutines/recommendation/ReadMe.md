# 1.需求说明
  简单的推荐视频系统：从用户的历史记录中获取标签权重，然后从时下最热电影中推荐一个给用户。
# 2.推荐因子
 (Ⅰ)、本人观看的历史记录所产生的标签权重；
 (Ⅱ)、时下最热视频的标签权重
# 3.推荐算法
 从推荐因子(Ⅱ)中选出最满足推荐因子(Ⅰ)的视频。
 使用kilim框架实现。
# 4.如何运行
 因为时采用的织入，所以必须要完成相应的打包：
  mvn package exec:java -Dexec.mainClass=com.mashibing.coroutines.recommendation.RecommendApp -Dexec.args=1
 或者：
   使用插件：exec-maven-plugin，然后根据插件的配置执行mvn test(本pom配置的test阶段)