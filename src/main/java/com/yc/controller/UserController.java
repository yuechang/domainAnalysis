/*
 * Copyright (c) 2018, 2025, Yue Chang and/or its affiliates. All rights reserved.
 * Yue Chang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.yc.controller;

import com.yc.aop.UserEntityAspect;
import com.yc.entity.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yue Chang
 * @version 1.0
 * @className: UserEntityAspect
 * @description: 用户控制器类
 * @date 2018年06月21日 19:27
 */
@RestController
public class UserController {

    public static String USER_SESSION_KEY = "userEntity";


    @GetMapping("/setUserEntity")
    public String setUserEntity(HttpServletRequest request) {

        UserEntity userEntity = new UserEntity();
        userEntity.setAge((short) 25);
        userEntity.setId(1000L);
        userEntity.setName("K");
        // 模拟登陆成功后，设置用户信息到session中
        request.getSession().setAttribute(USER_SESSION_KEY, userEntity);
        return userEntity.toString();
    }

    @GetMapping("/getUserEntity")
    public String getUserEntity() {

        UserEntity userEntity = UserEntityAspect.getUserEntity();
        if (null == userEntity) {
            return "未能获取用户信息";
        }
        return userEntity.toString();
    }
}
