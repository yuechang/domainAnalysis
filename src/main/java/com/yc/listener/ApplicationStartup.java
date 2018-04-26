/*!
 * Copyright(c) 2017 Yue Chang
 * MIT Licensed
 */
package com.yc.listener;

import com.yc.service.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author Yue Chang
 * @ClassName: ApplicationStartup
 * @Description: 检查域名监听器
 * @date 2018/4/23 21:27
 */
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>
{
    private static Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        try {
            ApplicationContext applicationContext = event.getApplicationContext();
            DomainService domainService = (DomainService)applicationContext.getBean("domainServiceImpl");

            domainService.beginCheckDomain();
        } catch (Exception e) {
            logger.info("检查域名是否可以出现异常", e);
        }
    }
}

