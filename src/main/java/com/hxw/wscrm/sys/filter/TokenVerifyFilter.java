package com.hxw.wscrm.sys.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hxw.wscrm.common.constant.SystemConstant;
import com.hxw.wscrm.common.result.JWTUtils;
import com.hxw.wscrm.sys.entity.SysUser;
import com.hxw.wscrm.sys.service.ISysMenuService;
import com.hxw.wscrm.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenVerifyFilter extends BasicAuthenticationFilter {

    private ISysMenuService sysMenuService;
    private ISysUserService sysUserService;

    public TokenVerifyFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public void setSysMenuService(ISysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    public void setSysUserService(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 校验提交的Token是否合法的方法
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取请求携带的Token信息
        String header = request.getHeader("Authorization");
        String path = request.getServletPath();
        // 跳过白名单路径
        if (path.contains("doc.html") || path.contains("webjars") ||
                path.contains("v2/") || path.contains("v3/") ||
                path.contains("swagger") || path.contains("api/auth")) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中没有Authorization信息则直接拦截
        if (header == null || !header.startsWith(SystemConstant.SYS_TOKEN_PREFIX)) {
            responseLogin(response);
            return;
        }

        try {
            // 1.获取到正常的token（去除前缀）
            String token = header.replace(SystemConstant.SYS_TOKEN_PREFIX, "");

            // 2.校验token信息是否合法
            DecodedJWT verify = JWTUtils.verify(token);

            // 3.如果验证失败会返回null，验证成功则继续处理
            if (verify == null) {
                responseLogin(response);
                return;
            }
            String userName = verify.getClaim("username").asString();

            // 4.从数据库中查询用户的实际权限
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (sysMenuService != null) {
                List<String> perms = sysMenuService.queryPermsByUserName(userName);
                if (perms != null && !perms.isEmpty()) {
                    for (String perm : perms) {
                        authorities.add(new SimpleGrantedAuthority(perm));
                    }
                }
            }
            // 如果没有查询到权限，添加默认权限（可选）
            if (authorities.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            // 5.查询用户信息
            SysUser user = null;
            if (sysUserService != null) {
                List<SysUser> users = sysUserService.queryByUserName(userName);
                if (users != null && !users.isEmpty()) {
                    user = users.get(0);
                }
            }

            // 6.创建认证信息并设置到安全上下文
            Object principal = user != null ? user : userName;
            UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 6.放行请求
            chain.doFilter(request, response);

        } catch (Exception e) {
            // 任何异常都视为token无效
            responseLogin(response);
        }
    }

    /**
     * 响应登录提示信息
     */
    private void responseLogin(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter writer = response.getWriter();
        Map<String, Object> res = new HashMap<>();
        res.put("code", HttpServletResponse.SC_FORBIDDEN);
        res.put("msg", "请先登录或token已失效");
        writer.write(JSON.toJSONString(res));
        writer.flush();
        writer.close();
    }
}
