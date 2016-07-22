package com.demo.commons.spring;

import com.demo.commons.passport.LoginInfo;
import com.demo.commons.passport.PassportUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 环境变量类,为了减少不必要的性能消耗,部分数据在调用时在进行处理
 *
 */
public class Context {
    // TODO 不适合存放整个request对象,只需要将需要的数据放入即可
    private HttpServletRequest request;

    private LoginInfo loginInfo;
//
//    private ClientInfo clientInfo;

    /**
     * 用户选择的城市
     */
    private Integer cityId;

    /**
     * 用户定位的城市
     */
    private Integer locationCityId;

    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 将request中需要用到的数据,存放到环境变量中
     *
     * @param request
     */
    public void setRequestContent(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 获取User-Agent
     *
     * @return
     */
    public String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    /**
     * 获取登录用户的id
     *
     * @return
     */
    public int getUserId() {
        // 只在使用的时候才进行获取
        synchronized (this) {
            if (loginInfo == null) {
                this.loginInfo = PassportUtil.getLoginInfo(request);
            }
        }

        if (loginInfo != null) {
            return loginInfo.getUserId();
        }

        return 0;
    }

    /**
     * 获取当前用户选择的城市
     *
     * @return
     */
//    public int getCityId() {
//        synchronized (this) {
//            if (cityId == null) {
//                String city = request.getHeader("pra-c-city");
//
//                if (city != null) {
//                    try {
//                        cityId = Integer.parseInt(city);
//                    } catch (Exception e) {
//
//                    }
//                }
//
//                if (cityId == null) {
//                    cityId = ConfigUtil.getPropertyInt("data.default_city");
//                }
//            }
//        }
//
//        return cityId;
//    }

    /**
     * 获取app系统定位到的城市
     *
     * @return
     */
//    public int getLocationCityId() {
//        synchronized (this) {
//            if (locationCityId == null) {
//                String city = request.getHeader("pra-l-city");
//
//                if (city != null) {
//                    try {
//                        locationCityId = Integer.parseInt(city);
//                    } catch (Exception e) {
//                    }
//                }
//
//                if (locationCityId == null) {
//                    locationCityId = ConfigUtil.getPropertyInt("data.default_city");
//                }
//            }
//        }
//
//        return locationCityId;
//    }

    /**
     * 获取客户端信息
     *
     * @return
     */
//    public ClientInfo getClientInfo() {
//        synchronized (this) {
//            if (clientInfo == null) {
//                this.clientInfo = CilentUtil.getClientInfo(request);
//            }
//        }
//
//        return clientInfo;
//    }
}
