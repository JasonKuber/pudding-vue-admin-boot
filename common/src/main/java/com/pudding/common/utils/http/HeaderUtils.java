package com.pudding.common.utils.http;

import cn.hutool.core.util.StrUtil;
import com.pudding.common.constants.http.HeaderConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取请求头内容工具类
 */
public class HeaderUtils {

    /**
     * 获取请求头中的 Authorization : Bearer {Token}
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



}
