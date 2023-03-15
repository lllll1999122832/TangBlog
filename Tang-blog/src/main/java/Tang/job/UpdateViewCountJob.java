package Tang.job;

import Tang.cache.RedisCache;
import Tang.pojo.Article;
import Tang.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Autowired
   private   RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount(){
        //获取redis中的浏览量
        Map<String, Integer> redisCacheRunner = redisCache.getCacheMap("article:viewCount");
        //把浏览量放入数据库中
        List<Article> articleList = redisCacheRunner.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey())
                        , entry.getValue().longValue()))
                .collect(Collectors.toList());
        articleService.updateBatchById(articleList);
    }
}
