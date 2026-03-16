package com.hxw.wscrm.pages.common;

import com.hxw.wscrm.sys.entity.SysUser;
import com.hxw.wscrm.sys.service.ISysUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class SecurityUtil {

    // 获取当前登录用户
    public static SysUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof SysUser) {
            return (SysUser) principal;
        } else if (principal instanceof String) {
            // 当principal是用户名时，尝试从认证信息中获取更多信息
            // 注意：这里不再通过sysUserService查询，因为可能会导致循环依赖
            // 直接返回null，让调用方处理这种情况
        }
        return null;
    }

    // 直接获取用户ID
    public static Long getUserId() {
        SysUser user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }
}
