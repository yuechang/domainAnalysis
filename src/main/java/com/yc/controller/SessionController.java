/*
 * Copyright (c) 2016, 2025, HHLY and/or its affiliates. All rights reserved.
 * HHLY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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

import java.util.HashMap;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
* @ClassName: SessionController
* @Description: 分布式session共享controller测试类
* @author Yue Chang 
* @date 2018年4月10日 下午4:36:05 
* @since 1.0
*/
@RestController
//@EnableScheduling
public class SessionController {
    
    @GetMapping("/setUrl")
    public Map<String,Object> setUrl(HttpServletRequest request){
        request.getSession().setAttribute("url", request.getRequestURL());
        Map<String,Object> map = new HashMap<>();
        map.put("url", request.getRequestURL());
        return map;
    }

    @GetMapping("/getSession")
    public Map<String,Object> getSession(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        map.put("url", request.getSession().getAttribute("url"));
        return map;
    }
    
	//@Scheduled(cron = "0 0/5 * * * ?") // 每五分钟执行一次
	public void beginCrawlerInfomation() {
		System.out.println("test");
	}
}
