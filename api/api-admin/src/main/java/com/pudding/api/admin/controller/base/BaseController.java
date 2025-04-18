package com.pudding.api.admin.controller.base;

import cn.hutool.core.util.ObjectUtil;
import com.pudding.common.security.AdminLoginUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 通用Controller
 */
public abstract class BaseController {


    /**
     * 从SecurityContextHolder 中获取当前登录用户
     */
    protected AdminLoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtil.isNull(authentication) || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof AdminLoginUser) {
            return (AdminLoginUser) principal;
        }
        return null;
    }

    /**
     * 获取当前登录用户Id
     */
    protected Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取当前登录用户角色id
     */
    protected Long getRoleId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取当前登录用户账户
     * @return
     */
    protected String getAccount() {
        return getLoginUser().getAccount();
    }

    /**
     * 获取当前登录用户用户名
     * @return
     */
    protected String getUserName() {
       return getLoginUser().getUsername();
    }

    protected Boolean isLogin() {
        return ObjectUtil.isNotNull(isLogin());
    }



}
