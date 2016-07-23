package com.demo.commons.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 线程池的统一声明
 *
 */
public class ThreadPool {

    /**
     * 用于重新加载配置数据的定时任务
     */
    public static ScheduledExecutorService configReloadThread = Executors.newSingleThreadScheduledExecutor();

    /**
     * 用于重新加载基础数据数据的定时任务
     */
    public static ScheduledExecutorService dataReloadThread = Executors.newSingleThreadScheduledExecutor();

    /**
     * 用于回收访问量控制的软引用
     */
    public static ExecutorService viewControlSoftClearThread = Executors.newFixedThreadPool(10);

    public static ScheduledExecutorService viewControlTimeOutClearThread = Executors.newSingleThreadScheduledExecutor();
}
