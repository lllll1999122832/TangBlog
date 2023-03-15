package Tang.handler;

import Tang.enums.AppHttpCodeEnum;
import Tang.pojo.ResponseResult;
import Tang.utils.WebUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
//认证异常统一配置
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
       //打印异常信息
        authException.printStackTrace();
        ResponseResult error=null ;
        //BadCredentialsException 用户名密码错误异常
        if(authException instanceof BadCredentialsException){
            //异常信息不可以写死
            error= ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),authException.getMessage());
        }else if(authException instanceof InsufficientAuthenticationException){
            error=ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN.getCode(),authException.getMessage());
            //InsufficientAuthenticationException 未携带token异常
        }else{
            error=ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"认证或者授权失败");
        }
        //响应前端
        WebUtils.renderString(response, JSON.toJSONString(error));
    }
}
