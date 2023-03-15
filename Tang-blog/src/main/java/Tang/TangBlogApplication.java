package Tang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("Tang.mapper")
@EnableScheduling //开启定时任务
@EnableSwagger2
public class TangBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(TangBlogApplication.class,args);
    }
}
