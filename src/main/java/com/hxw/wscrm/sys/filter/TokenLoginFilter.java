package com.hxw.wscrm.sys.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.hxw.wscrm.common.constant.SystemConstant;
import com.hxw.wscrm.common.result.JWTUtils;
import com.hxw.wscrm.sys.entity.SysUser;
import com.hxw.wscrm.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    
    @Autowired
    private ISysUserService sysUserService;
    
    @Autowired
    private com.hxw.wscrm.sys.service.ISysMenuService sysMenuService;

    public TokenLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    // 添加setter方法，以便在MySpringSecurityConfiguration中注入sysUserService
    public void setSysUserService(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }
    
    // 添加setter方法，以便在MySpringSecurityConfiguration中注入sysMenuService
    public void setSysMenuService(com.hxw.wscrm.sys.service.ISysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    /**
     * 具体认证的方法
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        SysUser sysUser = null;
        try {
            String loginInfo = getRequestJSON(request);
            sysUser = JSON.parseObject(loginInfo, SysUser.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword());
        return  authenticationManager.authenticate(authRequest);
    }

    private String getRequestJSON(HttpServletRequest request) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * 认证成功的方法
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //生成Token信息
        Map<String,String> map = new HashMap<>();
        String username = authResult.getName();
        map.put("username", username);
        
        // 通过用户名查询SysUser对象
        com.hxw.wscrm.sys.entity.SysUser user = null;
        try {
            // 使用sysUserService查询用户信息
            List<com.hxw.wscrm.sys.entity.SysUser> userList = sysUserService.queryByUserName(username);
            if (userList != null && !userList.isEmpty()) {
                user = userList.get(0);
                map.put("userId", user.getUserId().toString());
                
                // 查询用户的角色信息（通过sysUserService的方法）
                List<Integer> roleIds = sysUserService.getRoleIdsByUserId(user.getUserId());
                if (roleIds != null && !roleIds.isEmpty()) {
                    // 将角色ID列表转换为逗号分隔的字符串
                    StringBuilder roleIdsStr = new StringBuilder();
                    for (int i = 0; i < roleIds.size(); i++) {
                        if (i > 0) {
                            roleIdsStr.append(",");
                        }
                        roleIdsStr.append(roleIds.get(i));
                    }
                    map.put("roleIds", roleIdsStr.toString());
                }
            }
        } catch (Exception e) {
            // 查询失败时不影响登录，只是不存储userId和roleIds
        }
        
        //生成对应的Token信息
        String token = JWTUtils.getToken(map);
        //把生成的Token信息响应给客户端
        response.addHeader("Authorization", SystemConstant.SYS_TOKEN_PREFIX +token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        Map<String,Object> res = new HashMap<>();
        res.put("code",HttpServletResponse.SC_OK);
        res.put("msg","认证通过");
        // 在响应体中也返回用户信息
        if (user != null) {
            res.put("data", user);
            // 查询用户权限
            try {
                if (sysMenuService != null) {
                    List<String> perms = sysMenuService.queryPermsByUserName(username);
                    if (perms != null) {
                        Map<String, Object> userData = (Map<String, Object>) JSON.toJSON(user);
                        userData.put("perms", perms);
                        res.put("data", userData);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        writer.write(JSON.toJSONString(res));
        writer.flush();
        writer.close();
    }

    /**
     * 认证失败的方法
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        Map<String,Object> res = new HashMap<>();
        res.put("code",HttpServletResponse.SC_UNAUTHORIZED);
        res.put("msg","用户名或密码错误");
        writer.write(JSON.toJSONString(res));
        writer.flush();
        writer.close();
    }
}
