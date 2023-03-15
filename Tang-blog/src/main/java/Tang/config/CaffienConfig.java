package Tang.config;

import Tang.pojo.ResponseResult;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffienConfig {
    @Bean
    public Cache<Long, ResponseResult> ResponseResultCache(){
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(100000)
                .expireAfterAccess(2, TimeUnit.HOURS)
                .build();
    }
}
