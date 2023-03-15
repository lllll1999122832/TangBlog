package Tang.filter;

import Tang.enums.AppHttpCodeEnum;
import Tang.pojo.LoginUser;
import Tang.pojo.ResponseResult;
import Tang.utils.JwtUtil;
import Tang.utils.WebUtils;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        if(Objects.isNull(token)){
            //没有token,可能在登录中
            filterChain.doFilter(request,response);
            return;
        }
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            //token超时 token非法
//            throw new RuntimeException(e);
            //统一异常处理是针对controller,所以这里不能直接抛出异常
            ResponseResult result=ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            //返回错误信息
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //解析获取userId
        String userId = claims.getSubject();
        //从redis中获取信息 ,可能登录时间太久了
        String redis = stringRedisTemplate.opsForValue().get("bloglogin:" + userId);
        LoginUser loginUser = JSON.parseObject(redis, LoginUser.class);
        if(Objects.isNull(loginUser)){
            //redis中的信息超时了,需要重新登录
            ResponseResult result=ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            //返回错误信息
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        //第三个参数是权限的信息
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request,response);
    }
}
