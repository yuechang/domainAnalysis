/*!
 * Copyright(c) 2017 Yue Chang
 * MIT Licensed
 */
package com.yc.service.impl;

import com.yc.entity.ModuleEntity;
import com.yc.entity.ResultEntity;
import com.yc.service.DomainService;
import com.yc.task.StorageTask;
import com.yc.util.Constants;
import com.yc.util.HttpUtil;
import com.yc.util.JsonUtils;
import com.yc.util.MD5Util;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Yue Chang
 * @ClassName: DomainSericeImpl
 * @Description: 域名检查服务实现类
 * @date 2018/4/23 16:31
 */
@Service
public class DomainServiceImpl implements DomainService {

    private static Logger logger = LoggerFactory.getLogger(DomainServiceImpl.class);

    private volatile static AtomicLong count = new AtomicLong(0L);
    private volatile static AtomicLong availableCount = new AtomicLong(0L);
    private static ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

    @Autowired
    private StorageTask storageTask;

    @Override
    public void beginCheckDomain() throws Exception {

        List<Callable<Long>> callableList = new ArrayList<>();
        for (int i = 0; i < Constants.LETTERS.length; i++){
            String letter = Constants.LETTERS[i];
            final long number = i;
            Callable<Long> thread = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    String[] letterArray = new String[1];
                    letterArray[0] = letter;
                    checkDomain(letterArray);
                    return number;
                }
            };
            callableList.add(thread);
        }

        List<Future<Long>> futureList = newCachedThreadPool.invokeAll(callableList);
        for (Future<Long> future : futureList) {
            Long result = future.get();
            System.out.println(result);
        }
        logger.info("已完成所有检查：" + count.get());
    }

    /**
     * @category 检查域名是否可用
     * @since 20180421
     * @author Yue Chang
     * @date 2018年4月22日 下午9:33:20
     * @throws Exception
     */
    private void checkDomain(String[] domainArrange) throws Exception {
        for (String first : domainArrange) {
            for (String second : Constants.LETTERS) {
                for (String thrid : Constants.LETTERS) {
                    for (String fourth : Constants.LETTERS) {
                        for(String fifth : Constants.LETTERS) {
                            for (String tid : Constants.DOMAIN_TID) {
                                String domain = MessageFormat.format("{0}{1}{2}{3}{4}.{5}", first,second,thrid,fourth,fifth,tid);
                                doCheckDomain(domain);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @category 检查域名是否可用
     * @since 20180421
     * @author Yue Chang
     * @date 2018年4月21日 上午10:28:36
     * @throws Exception
     */
    private void doCheckDomain(String domain) throws Exception {

        if(StringUtils.isBlank(domain)) {
            return;
        }

        long number = count.incrementAndGet();
        if(number%1000 == 0) {
            logger.info("检查域名数目：" + number);
        }
        Map<String, String> params = new HashMap<>();
        params.put(Constants.TOKEN_KEY, "Y".concat(MD5Util.getMD5(domain)));
        params.put(Constants.DOMAIN_KEY, domain);

        String resultJson = HttpUtil.doGetWithProxy(Constants.CHECK_DOMAIN_URL, params, Constants.DEFAULT_CHARSET);
        if(StringUtils.isBlank(resultJson)) {
            return;
        }

        ResultEntity resultEntity = JsonUtils.json2pojo(resultJson,ResultEntity.class);
        if(null == resultEntity) {
            return;
        }
        List<ModuleEntity> moduleList = resultEntity.getModule();
        if(CollectionUtils.isEmpty(moduleList)) {
            return;
        }
        for (ModuleEntity moduleEntity : moduleList) {
            if(null == moduleEntity || 1 != moduleEntity.getAvail())
                continue;
            // 记录可注册域名
            logger.info(moduleEntity.toString());
            // 可注册域名数量
            long sum = availableCount.incrementAndGet();
            if(sum%1000 == 0){
                logger.info("已检查到可注册域名数量为：" + sum);
            }
            // 将可注册域名信息放入阻塞队列
            storageTask.put(moduleEntity);
        }
    }
}

