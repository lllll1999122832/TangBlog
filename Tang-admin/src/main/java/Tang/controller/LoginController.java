package Tang.controller;

import Tang.cache.RedisCache;
import Tang.enums.AppHttpCodeEnum;
import Tang.exception.SystemException;
import Tang.pojo.LoginUser;
import Tang.pojo.Menu;
import Tang.pojo.ResponseResult;
import Tang.pojo.User;
import Tang.service.BlogLoginService;
import Tang.service.LoginService;
import Tang.service.MenuService;
import Tang.service.RoleService;
import Tang.utils.BeanCopyUtils;
import Tang.utils.SecurityUtils;
import Tang.vo.AdminUserInfoVo;
import Tang.vo.RoutersVo;
import Tang.vo.UserInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    MenuService menuService;
    @Autowired
    RoleService roleService;


    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUsername())){
           throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserInfoVo user = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        //根据用户id查询权限信息
        List<String> perms= menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList= roleService.selectRoleByUserId(loginUser.getUser().getId());
        //封装
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList,user);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouter(){
        //查询Menu,结果是tree的形式
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<Menu> menus=menuService.selectRouterMenuTreeByUserId(loginUser.getUser().getId());
        //封装数据
        return ResponseResult.okResult(new RoutersVo(menus));
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
