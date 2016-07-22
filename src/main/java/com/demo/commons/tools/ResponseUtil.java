package com.demo.commons.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * @author chen.lin
 * @date 14/11/25
 * @time 下午8:49
 */
public class ResponseUtil {
    /**
     * 向浏览器返回一个ajax的响应,json格式
     *
     * @param data
     */
    public static void writeResponse(HttpServletResponse response, Object data) {
        Writer writer = null;
        try {
            setHtmlContentNoCacheHeader(response);
            writer = response.getWriter();
            writer.write(JSON.toJSONString(data, SerializerFeature.DisableCircularReferenceDetect));
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * 设置响应的内容类型及编码，同时设置不缓存的头信息
     *
     * @param response
     */
    public static void setHtmlContentNoCacheHeader(HttpServletResponse response) {
        setContentTypeHeader(response);
        setNoCacheHeader(response);
    }

    /**
     * 设置不缓存响应头
     *
     * @param response http响应
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache,no-store");
        response.setDateHeader("Expires", 0);
    }

    /**
     * 设置content-type header以及响应编码,编码从response中的getCharacterEncoding获取编码，如果此编码为空则设置为默认编码（默认为UTF-8）
     *
     * @param response http响应
     */
    public static void setContentTypeHeader(HttpServletResponse response) {
        String encoding = response.getCharacterEncoding();
        if (StringUtils.isBlank(encoding)) {
            encoding = "utf-8";
            response.setCharacterEncoding(encoding);
        }
        response.setContentType("application/json;charset=" + encoding);
    }
}
