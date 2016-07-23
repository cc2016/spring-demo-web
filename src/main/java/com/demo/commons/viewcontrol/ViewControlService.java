package com.demo.commons.viewcontrol;

import com.demo.commons.logger.APILog;
import com.demo.commons.tools.ConfigUtil;
import com.demo.commons.tools.ThreadPool;
import org.apache.commons.lang3.StringUtils;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ViewControlService {

    /**
     * 存放访问量
     */
    private Map<SoftKey, Value> viewCount = new ConcurrentHashMap<SoftKey, Value>();

    /**
     * 回收被系统删除的软引用
     */
    private ReferenceQueue<Key> queue = new ReferenceQueue<Key>();

    /**
     * 缓存过期的key
     */
    private String intervalKey;

    /**
     * 最大访问数量的key
     */
    private String maxKey;

    /**
     * 构造方法
     */
    public ViewControlService(String maxKey, String intervalKey) {
        if (StringUtils.isEmpty(maxKey)) {
            maxKey = "data.viewcontrol.max";
        }

        if (StringUtils.isEmpty(intervalKey)) {
            intervalKey = "data.viewcontrol.interval";
        }

        this.maxKey = maxKey;
        this.intervalKey = intervalKey;

        // 开启线程,清除被清理的soft引用
        new Thread() {
            public void run() {
                removeViewCount();
            }
        }.start();

        // 定时清除时间过期的缓存
        ThreadPool.viewControlTimeOutClearThread.schedule(clearTimeOutViewCount, 3600, TimeUnit.SECONDS);
    }

    /**
     * 删除软引用的线程
     */
    private void removeViewCount() {
        while (true) {
            try {
                // TODO 存在一个问题,如果访问量特别大的时候,创建对象的数据跟不上删除速度,会造成一直fullgc
                final Reference<? extends Key> skey = queue.remove();

                if (skey != null) {
                    ThreadPool.viewControlSoftClearThread.execute(new Runnable() {
                        /**
                         * 是否在快速清理队列中
                         */
                        private boolean cleaning = false;

                        public void run() {
                            BlockingQueue<Runnable> exeQueue = ((ThreadPoolExecutor) ThreadPool.viewControlSoftClearThread).getQueue();
                            int queueSize = ((ThreadPoolExecutor) ThreadPool.viewControlSoftClearThread).getQueue().size();

                            if (queueSize > 10000) {
                                if (!cleaning) {
                                    // 如果等待处理的队列很大,那么直接清空viewcount和队列
                                    viewCount.clear();
                                    exeQueue.clear();

                                    // 设置状态为清空中,队列中的任务快速消除,不做任何逻辑
                                    cleaning = true;
                                }
                            } else {
                                viewCount.remove(skey);

                                if (queueSize < 500) {
                                    // 设置清空状态为false
                                    cleaning = false;
                                }
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                APILog.warn("", e);
            }

        }
    }

    /**
     * 定时清除过期的数据
     */
    private Runnable clearTimeOutViewCount = new Runnable() {
        public void run() {
            long now = System.currentTimeMillis();
            Iterator<Map.Entry<SoftKey, Value>> it = viewCount.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<SoftKey, Value> e = it.next();

                Value value = e.getValue();

                if (value != null) {
                    int interval = ConfigUtil.getPropertyInt(intervalKey);
                    if (now > value.getMs() + interval) {
                        it.remove();
                    }
                }
            }
        }
    };

    /**
     * 增加访问量,返回false表示已经超出限制,不必处理
     *
     * @param ip
     * @return
     */
    public boolean addView(String ip) {
        long ms = System.currentTimeMillis();

        int max = ConfigUtil.getPropertyInt(maxKey);
        int interval = ConfigUtil.getPropertyInt(intervalKey);

        SoftKey key = new SoftKey(ip, queue);
        Value value = viewCount.get(key);

        // 是否进行初始化
        boolean needInit = false;

        if (value == null) {
            // 初始化数据
            needInit = true;
        } else {
            if (ms > value.getMs() + interval) {
                // 如果统计数据超过指定的时间间隔,重新初始化数据
                needInit = true;
            }
        }

        if (needInit) {
            synchronized (ip) {
                // 加锁,减少重复创建,创建新数据,覆盖老数据
                value = new Value();
                value.setMs(ms);

                // 创建软引用,放入map中
                viewCount.put(key, value);
            }
        }

        // 当前访问量加1
        int nowTotal = value.getCount().incrementAndGet();
        if (nowTotal > max) {
            // 如果访问量大于当前的限制,那么访问终止
            return false;
        }

        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        ViewControlService service = new ViewControlService(null, null);

        int count = 0;

        System.out.println("start");

        for (int i = 0; i < 100000; i++) {
            service.addView("111" + new Random().nextInt());
        }

        System.out.println("finish hot run");

        long start = System.currentTimeMillis();

        while (true) {
            if (!service.addView("111" + new Random().nextInt())) {
                break;
            }

            count++;

            if (count > 100000) {
                break;
            }
        }

        System.out.println(count / (float)(System.currentTimeMillis() - start));

        start = System.currentTimeMillis();

        count = 0;
        service.viewCount.clear();

        while (true) {
            if (!service.addView("111" + new Random().nextInt())) {
                break;
            }

            count++;

            if (count > 100000) {
                break;
            }
        }

        System.out.println(count / (float)(System.currentTimeMillis() - start));

        Thread.sleep(100);

        System.out.println("end");

    }


}
