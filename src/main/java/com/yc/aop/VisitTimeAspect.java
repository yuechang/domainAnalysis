package com.yc.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class VisitTimeAspect {

    private static Logger logger = LoggerFactory.getLogger(VisitTimeAspect.class);

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>(){
        // 设置初始值
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    @Pointcut("execution(public * com.yc.controller.*.*(..))")
    public void visitTimePointcut(){}

    @Before("visitTimePointcut()")
    public void doBefore() throws Throwable {
        // 设置开始访问时间
        threadLocal.set(System.currentTimeMillis());
    }

    @After(value = "visitTimePointcut()")
    public void doAfter(JoinPoint joinPoint) throws Throwable {

        try {
            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            // 获得请求开始系统时间
            Long startTime = threadLocal.get();
            // 计算访问时间
            double second = (System.currentTimeMillis() - startTime) / 1000.0;

            // 记录下请求内容
            logger.info("URL : " + request.getRequestURL().toString() + ", cost : " + second + "s ");
            logger.info("HTTP_METHOD : " + request.getMethod());
            logger.info("IP : " + request.getRemoteAddr());
            logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        } finally {
            // 访问结束，清除ThreadLocal中的值，避免产生OOM
            threadLocal.remove();
        }
    }
}
