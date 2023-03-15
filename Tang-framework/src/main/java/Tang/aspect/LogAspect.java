package Tang.aspect;

import Tang.annotation.SystemLog;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(Tang.annotation.SystemLog)")
    public void pt(){}
    @Around("pt()")
    //这里不可用try catch 不然controller方法的异常会被aop处理,没有办法被统一异常处理
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res;
        try {
            handleBefore(joinPoint);
             res = joinPoint.proceed();
             handleAfter(res);
        }finally {
            //结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }
        return res;
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        //封装了请求信息
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes ) RequestContextHolder.getRequestAttributes();//
        HttpServletRequest request = requestAttributes.getRequest();
        //获取被增强方法上的注解对象
        SystemLog systemLog=getSystemLog(joinPoint);
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}",systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
//         打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName());
//        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
//        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));

    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        //获取签名信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SystemLog annotation = signature.getMethod().getAnnotation(SystemLog.class);
        return annotation;
    }

    private void handleAfter(Object res) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(res));
    }
}
