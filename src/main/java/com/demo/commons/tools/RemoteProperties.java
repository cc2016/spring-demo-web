package com.demo.commons.tools;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 远程配置.
 * TODO 可能需要重写更多方法
 *
 *
 */
public class RemoteProperties extends Properties {

    /**
     * 使用concurrenthashmap替换hashtable
     */
    public volatile Map<String, String> map = new ConcurrentHashMap<String, String>();

    /**
     * 本地配置数据
     */
    public ConcurrentHashMap<String, String> local = new ConcurrentHashMap<String, String>();

    /**
     * 服务器配置数据
     */
    public ConcurrentHashMap<String, String> remote = new ConcurrentHashMap<String, String>();

    public String getProperty(String key) {
        return map.get(key);
    }

    public Object setProperty(String key, String value) {
        return map.put(key, value);
    }

    public int size() {
        return map.size();
    }

    /**
     * 更新本地配置
     *
     * @param properties
     */
    public void putAllLocal(Properties properties) {
        if (properties == null) {
            return;
        }

        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();

        this.local = new ConcurrentHashMap<String, String>();

        while (it.hasNext()) {
            Map.Entry<Object, Object> e = it.next();
            this.local.put(e.getKey().toString(), e.getValue().toString());
        }

        ConcurrentHashMap<String, String> tmp = new ConcurrentHashMap<String, String>();
        tmp.putAll(local);
        tmp.putAll(remote);

        map = tmp;
    }

    /**
     * 更新服务器配置
     *
     * @param properties
     */
    public synchronized void putAllRemote(Map<String, String> properties) {
        if (properties == null) {
            return;
        }

        this.remote = new ConcurrentHashMap<String, String>();
        this.remote.putAll(properties);

        ConcurrentHashMap<String, String> tmp = new ConcurrentHashMap<String, String>();
        tmp.putAll(local);
        tmp.putAll(remote);

        map = tmp;
    }

    /**
     * 更新服务器配置
     *
     * @param key
     * @param value
     */
    public synchronized void putRemote(String key, String value) {
        remote.put(key, value);
        map.put(key, value);
    }

}
