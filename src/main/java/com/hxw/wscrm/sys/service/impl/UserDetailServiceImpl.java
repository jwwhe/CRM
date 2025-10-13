package com.hxw.wscrm.sys.service.impl;

import com.hxw.wscrm.sys.entity.SysRole;
import com.hxw.wscrm.sys.entity.SysUser;
import com.hxw.wscrm.sys.service.ISysRoleService;
import com.hxw.wscrm.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证校验的方法
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    ISysRoleService sysRoleService;
    /**
     * 完成账号的校验
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //需要根据账号去查询
       List<SysUser> list =  sysUserService.queryByUserName(username);
       if (list != null && list.size() == 1) {
            //账号是存在的
           SysUser sysUser = list.get(0);
           //根据当前登录的账号查询角色的信息
           List<SysRole> sysRoles = sysRoleService.queryByUserId(sysUser.getUserId());
           List<GrantedAuthority> listRole = new ArrayList<>();
           if (sysRoles != null && sysRoles.size() > 0) {
               for (SysRole sysRole : sysRoles) {
                   listRole.add(new SimpleGrantedAuthority(sysRole.getRoleName()));
               }
           }
           return new User(sysUser.getUsername(), sysUser.getPassword(), listRole);
       }
        return null;
    }
}
