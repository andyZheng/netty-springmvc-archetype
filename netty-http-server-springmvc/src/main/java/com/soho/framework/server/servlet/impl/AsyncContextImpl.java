/*
 * 文件名：RapidAsyncContext.java
 * 版权：Copyright 2013-2015 ChengDu MS Leagues Studio. All Rights Reserved. 
 * 描述： 服务器框架
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.soho.framework.server.servlet.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * 功能描述：<code>AsyncContextImpl</code>基于{@link AsyncContext}提供异步请求服务。</p>
 *
 * @author andy.zheng0807@gmail.com
 * @version 1.0, 2015年5月4日 下午6:58:33
 * @since Game Server Framework/Context 1.0
 */
public class AsyncContextImpl implements AsyncContext {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    
    private final List<AsyncListener> listeners = new ArrayList<AsyncListener>();

    private long timeout = 10 * 1000L;  // 10 seconds is Tomcat's default
    
    private final List<Runnable> dispatchHandlers = new ArrayList<Runnable>();

    public AsyncContextImpl(ServletRequest servletRequest, ServletResponse servletResponse) {
        this.request = (HttpServletRequest)servletRequest;
        this.response = (HttpServletResponse)servletResponse;
    }
    
    public void addDispatchHandler(Runnable handler) {
        Assert.notNull(handler);
        this.dispatchHandlers.add(handler);
    }

    @Override
    public ServletRequest getRequest() {
        return this.request;
    }

    @Override
    public ServletResponse getResponse() {
        return this.response;
    }

    @Override
    public boolean hasOriginalRequestAndResponse() {
        return (this.request instanceof HttpServletRequest && this.response instanceof HttpServletResponse);
    }

    @Override
    public void dispatch() {
        this.dispatch(this.request.getRequestURI());
    }

    @Override
    public void dispatch(String path) {
        this.dispatch(null, path);
    }

    @Override
    public void dispatch(ServletContext context, String path) {
        for (Runnable r : this.dispatchHandlers) {
            r.run();
        }
    }

    @Override
    public void complete() {
        for (AsyncListener listener : this.listeners) {
            try {
                listener.onComplete(new AsyncEvent(this, this.request, this.response));
            }
            catch (IOException e) {
                throw new IllegalStateException("AsyncListener failure", e);
            }
        }
    }

    @Override
    public void start(Runnable handler) {
        handler.run();
    }

    @Override
    public void addListener(AsyncListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void addListener(AsyncListener listener, ServletRequest servletRequest, ServletResponse servletResponse) {
        this.listeners.add(listener);
    }

    @Override
    public <T extends AsyncListener> T createListener(Class<T> clazz) throws ServletException {
        return BeanUtils.instantiateClass(clazz);
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

}
