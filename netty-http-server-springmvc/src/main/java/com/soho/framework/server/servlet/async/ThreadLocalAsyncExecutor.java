package com.soho.framework.server.servlet.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;

public class ThreadLocalAsyncExecutor implements AsyncTaskExecutor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final ThreadLocal<Object> threadLocalResponses = new ThreadLocal<Object>();
    private static final ConcurrentHashMap<Object, Runnable> futureTasks = new ConcurrentHashMap<Object, Runnable>();

    @Override
    public void execute(Runnable task) {
        logger.info("Execute task: {}", task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        logger.info("Execute task: {} , start time out: {}", task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        logger.info("Submit runnable: {}", task);
        
        FutureTask<Object> future = new FutureTask<>(task, null);
        Object response = threadLocalResponses.get();
        futureTasks.putIfAbsent(response, future);
        
        return future;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        logger.info("Submit callable: {}", task);
        
        FutureTask<T> future = new FutureTask<>(task);
        Object response = threadLocalResponses.get();
        futureTasks.putIfAbsent(response, future);
        
        return future;
    }
    
    public static void setResponse(Object response){
        threadLocalResponses.set(response);
    }
    
    public static Runnable pollTask(Object response) {
        Runnable task = futureTasks.get(response);
        futureTasks.remove(response);
        
        return task;
    }
}
