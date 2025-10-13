package com.hxw.wscrm.pages.common;

import com.hxw.wscrm.sys.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    // 获取当前登录用户
    public static SysUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SysUser)) {
            return null;
        }
        return (SysUser) authentication.getPrincipal();
    }

    // 直接获取用户ID
    public static Long getUserId() {
        SysUser user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }
}
