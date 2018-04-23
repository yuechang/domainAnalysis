/*!
 * Copyright(c) 2017 Yue Chang
 * MIT Licensed
 */
package com.yc.task;

import com.yc.entity.ModuleEntity;
import com.yc.util.ExcelUtils;
import com.yc.util.OSinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Yue Chang
 * @ClassName: StorageTask
 * @Description: 存储域名线程
 * @date 2018/4/23 16:39
 */
@Component
public class StorageTask implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(StorageTask.class);

    private final BlockingQueue<ModuleEntity> queue = new LinkedBlockingQueue<>();

    // 初始化方法
    @PostConstruct
    public void init() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) try {
            ModuleEntity moduleEntity = queue.take();
            if (null == moduleEntity)
                continue;
            List<ModuleEntity> moduleEntityList = new ArrayList<>();
            moduleEntityList.add(moduleEntity);

            String filePath = "/data/app/domainAnalysis/domain.xlsx";
            if (OSinfo.isWindows()) {
                filePath = "D:\\domain.xlsx";
            }
            ExcelUtils.writeModuleEntityListToExcel(filePath, ModuleEntity.ATTRIBUTES_TITLE,
                    moduleEntityList, ModuleEntity.DOMAIN_INFO);


        } catch (Throwable e) {
            logger.error("存储可注册域名失败，" + e.getMessage(), e);
        }
    }

    public void put(ModuleEntity moduleEntity){
        try {
            queue.put(moduleEntity);
        } catch (InterruptedException e) {
            logger.error("放入队列失败，:" + e.getMessage(),e);
        }
    }
}

