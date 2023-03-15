package Tang.runner;

import Tang.cache.RedisCache;
import Tang.mapper.ArticleMapper;
import Tang.pojo.Article;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RedisCacheRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息 id viewCount
        List<Article> list = articleMapper.selectList(null);
        //Long 如1L在redis中没有办法递增
//        List<Integer> collect = list.stream().map(o -> o.getId().intValue()).collect(Collectors.toList());
        Map<String, Integer> collect = list.stream().collect(Collectors.toMap(o -> o.getId().toString(), o -> o.getViewCount().intValue()));
        //储存到redis中
        redisCache.setCacheMap("article:viewCount",collect);
    }
}
