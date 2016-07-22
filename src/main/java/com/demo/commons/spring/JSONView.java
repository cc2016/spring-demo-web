package com.demo.commons.spring;

import com.demo.commons.constants.SpringContents;
import com.demo.commons.tools.ResponseUtil;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 将spring方法的返回值,处理为json格式,返回前端
 *
 */
public class JSONView implements View {

    private Object emptyResult = new Object();

    public String getContentType() {
        return null;
    }

    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object obj = request.getAttribute(SpringContents.SPRING_KEY_RETURN);

        JSONResult result = null;

        if (obj instanceof NoResult) {
            // 如果是NoResult,那么不做任何处理
            return;
        } else if (obj instanceof JSONResult) {
            // 直接返回JSONResult对象
            result = (JSONResult) obj;
        } else if (obj instanceof String) {
            // 如果返回字符串,填充到message中
            result = new JSONResult();
            result.setMessage((String) obj);
            result.setCode(ResponseCodes.SUCCESS);
        } else {
            // 如果返回数据对象,填充到data中
            result = new JSONResult();
            result.setData(obj);
            result.setCode(ResponseCodes.SUCCESS);
        }

        if (result.getMessage() == null) {
            // 防止返回值为空
            result.setMessage("");
        }

        if (result.getData() == null) {
            // 如果返回值是空的话,那么使用一个空的对象来填充,防止前端的json没有data字段
            result.setData(emptyResult);
        }

        ResponseUtil.writeResponse(response, result);
    }
}
