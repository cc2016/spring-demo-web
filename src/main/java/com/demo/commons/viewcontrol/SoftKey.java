package com.demo.commons.viewcontrol;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * 软引用
 */
public class SoftKey extends SoftReference<Key> {

    private Integer hashCode;

    public SoftKey(String ip, ReferenceQueue<Key> queue) {
        super(new Key(ip), queue);
        hashCode = ip.hashCode();
    }

    public int hashCode() {
        return hashCode;
    }

    public boolean equals(Object o) {
        if (o instanceof SoftKey) {
            return hashCode.equals(o.hashCode());
        }

        return false;
    }
}
