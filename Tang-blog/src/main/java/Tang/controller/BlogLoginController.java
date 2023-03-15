package Tang.controller;

import Tang.enums.AppHttpCodeEnum;
import Tang.exception.SystemException;
import Tang.pojo.ResponseResult;
import Tang.pojo.User;
import Tang.service.BlogLoginService;
import Tang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {
    @Autowired
    BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUsername())){
           throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        System.out.println("到这里");
        return blogLoginService.login(user);
    }
    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
