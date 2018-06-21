package com.yc.aop;

import com.yc.controller.UserController;
import com.yc.entity.UserEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class UserEntityAspect {

    private static Logger logger = LoggerFactory.getLogger(UserEntityAspect.class);

    private static ThreadLocal<UserEntity> threadLocal = new ThreadLocal<>();

    @Pointcut("execution(public * com.yc.controller.*.*(..))")
    public void userEntityPointcut(){}

    @Before("userEntityPointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 从session里面获取对应的用户信息，设置到ThreadLocal中
        Object object = request.getSession().getAttribute(UserController.USER_SESSION_KEY);
        if (null != object && object instanceof UserEntity) {
            UserEntity userEntity = (UserEntity) object;
            threadLocal.set(userEntity);
        }

    }

    @After(value = "userEntityPointcut()")
    public void doAfter() throws Throwable {

        // 访问结束，清除ThreadLocal中的值，避免产生OOM
        threadLocal.remove();
    }

    /**
     * 获取ThreadLocal中的用户信息
     */
    public static UserEntity getUserEntity() {
        return threadLocal.get();
    }
}
