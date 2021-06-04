package com.ral.server.system.thread;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class GameThreadPool {

    public static ThreadPoolExecutor cmdPool;


    /**
     * 初始化线程池
     */
    @PostConstruct
    public void GameThreadPool(){
        int cpuNum = Runtime.getRuntime().availableProcessors();
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
        cmdPool = new ThreadPoolExecutor(cpuNum * 2, cpuNum * 4, 5, TimeUnit.MINUTES, workQueue, handler);
    }


    /**
     *执行任务
     * @param task
     */
    public static void executeTask(Runnable task){
        cmdPool.execute(task);
    }




}
