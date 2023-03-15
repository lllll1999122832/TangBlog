package Tang.service.impl;

import Tang.cache.RedisCache;
import Tang.pojo.LoginUser;
import Tang.pojo.ResponseResult;
import Tang.pojo.User;
import Tang.service.LoginService;
import Tang.utils.BeanCopyUtils;
import Tang.utils.JwtUtil;
import Tang.utils.SecurityUtils;
import Tang.vo.BlogUserLoginVo;
import Tang.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new
//                UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
                UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        //默认是从内存中查询,但我们需要从数据库中查询,所以需要自定义 实现UserDetailService
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //判断认证是否成功
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户认证失败,用户名不存在或者密码错误");
        }
        //获取userId,生成token
        //假如不知道Principal有什么属性可以在这里打断点调试
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String token = JwtUtil.createJWT(String.valueOf(loginUser.getUser().getId()));
        //把用户信息存入redis
        String redis="login:"+String.valueOf(loginUser.getUser().getId());
        redisCache.setCacheObject(redis,loginUser);
       //返回token
        Map<String,String>map=new HashMap<>();
        map.put("token",token);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //获取当前用户的id
        Long id = SecurityUtils.getLoginUser().getUser().getId();
        //删除redis中的信息
        redisCache.deleteObject("login:"+id);
        return ResponseResult.okResult();
    }

}
