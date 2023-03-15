package Tang.controller;

import Tang.annotation.SystemLog;
import Tang.pojo.ResponseResult;
import Tang.pojo.User;
import Tang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }
    @PutMapping("/userInfo")
    @SystemLog(businessName = "用户更新信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }
    @PostMapping("/register")
    @SystemLog(businessName = "用户注册信息")
    public ResponseResult register(@RequestBody User user){
        System.out.println(user);
        return userService.register(user);
    }
}
