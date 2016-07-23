package com.demo.commons.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.commons.logger.APILog;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 读取配置文件的工具类,支持从远程配置服务器获取配置数据.
 * 远程服务器的配置,放置在服务器的/data/env/app.env文件中
 *
 */
public class ConfigUtil extends PropertyPlaceholderConfigurer {

    /**
     * 配置服务器的地址
     */
    private static String configServer;

    /**
     * 本地环境的id
     */
    private static int envId;

    /**
     * 配置内容
     */
    private static RemoteProperties properties;

    private static Runnable reloadRunner = new Runnable() {
        public void run() {
            // 从远程加载配置,保存到本地
            Map<String, String> pro = getConfigFromServer();

            if (pro != null) {
                properties.putAllRemote(pro);
            }
        }
    };

    /**
     * 定时从服务器获取新的配置数据
     */
    private static void startReload() {
        ThreadPool.configReloadThread.scheduleAtFixedRate(reloadRunner, 10, 15, TimeUnit.SECONDS);
    }

    /**
     * 从远程服务器获取配置
     */
    private static Map<String, String> getConfigFromServer() {
        Map<String, String> properties = new HashMap<String, String>();

        try {
            Properties local = new Properties();
            local.load(new FileInputStream("/data/env/app.env"));

            configServer = local.getProperty("configServer");
            configServer += "/config/getConfigByEnv.html";

            String envIdStr = local.getProperty("appEnv");

            Map<String, String> param = new HashMap<String, String>();
            param.put("envId", envIdStr);

            String result = HttpClientUtil.getUriContentUsingGet(configServer, param);

            JSONObject jsonResult = JSON.parseObject(result);

            JSONArray envsResult = (JSONArray) jsonResult.get("configs");

            for (Object obj : envsResult) {
                JSONObject jsonObject = (JSONObject) obj;
                String key = jsonObject.getString("key");
                String value = jsonObject.getString("value");

                if (key != null && value != null) {
                    properties.put(key, value);
                }
            }

        } catch (Exception e) {
            APILog.error("", e);
        }

        return properties;
    }

    /**
     * 初始化配置文件
     *
     * @return
     * @throws IOException
     */
    protected Properties mergeProperties() throws IOException {
        RemoteProperties allPro = new RemoteProperties();

        try {
            // 获取本地配置
            Properties pro = super.mergeProperties();

            if (pro != null) {
                allPro.putAllLocal(pro);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 获取远程配置
            Map<String, String> serverConfig = getConfigFromServer();

            if (serverConfig != null) {
                allPro.putAllRemote(serverConfig);
            }
        } catch (Exception e) {
            APILog.error("", e);
        }

        // 赋值给静态变量,可以直接使用
        ConfigUtil.properties = allPro;

        // 定时从服务器重新加载数据
        startReload();

        return allPro;
    }

    /**
     * 获取配置
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        if (properties == null) {
            properties = new RemoteProperties();
            properties.putAllRemote(getConfigFromServer());
        }

        return properties.getProperty(key);
    }

    /**
     * 获取配置
     *
     * @param key
     * @return
     */
    public static int getPropertyInt(String key) {
        String value = getProperty(key);

        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                APILog.error("", e);
            }
        }

        return 0;
    }
}
