package com.demo.commons.viewcontrol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class Value {

    /**
     * 当前的毫秒数
     */
    private long ms;

    /**
     * 访问数量
     */
    private AtomicInteger count = new AtomicInteger();

    public AtomicInteger getCount() {
        return count;
    }


    public long getMs() {
        return ms;
    }

    public void setMs(long ms) {
        this.ms = ms;
    }
}
