package com.pudding.common.utils.http;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import com.pudding.common.constants.http.HeaderConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取请求头内容工具类
 */
public class HeaderUtils {


    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headers.put(key, value);
        }
        return headers;
    }


    /**
     * 获取请求头中的 Authorization : Bearer {Token}
     *
     * @return Token
     */
    public static String getAuthorizationBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HeaderConstants.AUTHORIZATION);
        // 验证头是否存在且格式正确
        if (StrUtil.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(HeaderConstants.AUTHORIZATION_TYPE_BEARER)) {
            return authorizationHeader.substring(7); // 截取 Token 部分
        }
        return null;
    }

    /**
     * 获取请求头中的 User-Agent
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(Header.USER_AGENT.getValue());
    }


}
