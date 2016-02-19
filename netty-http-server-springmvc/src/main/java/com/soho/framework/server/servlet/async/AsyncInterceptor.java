/*
 * 文件名：AsyncInterceptor.java
 * 版权：Copyright 2013-2015 ChengDu MS Leagues Studio. All Rights Reserved. 
 * 描述： 服务器框架
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.soho.framework.server.servlet.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;

import java.util.concurrent.Callable;

public class AsyncInterceptor implements CallableProcessingInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public <T> void beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception {
        logger.info("beforeConcurrentHandling " + request.getNativeRequest() + " " + request.getNativeResponse());
        ThreadLocalAsyncExecutor.setResponse(request.getNativeResponse());
    }

    @Override
    public <T> void preProcess(NativeWebRequest request, Callable<T> task) throws Exception {
    }

    @Override
    public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {
    }

    @Override
    public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
        return null;
    }

    @Override
    public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {
    }

}
