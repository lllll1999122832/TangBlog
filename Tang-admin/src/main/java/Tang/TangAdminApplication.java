package Tang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan("Tang.mapper")
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限控制  @PreAuthorize()注解有用
public class TangAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(TangAdminApplication.class,args);
    }
}
