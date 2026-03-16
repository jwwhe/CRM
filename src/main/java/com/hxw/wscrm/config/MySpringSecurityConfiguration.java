package com.hxw.wscrm.config;

import com.hxw.wscrm.sys.filter.TokenLoginFilter;
import com.hxw.wscrm.sys.filter.TokenVerifyFilter;
import com.hxw.wscrm.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


/**
 * SpringSecurity的配置成类
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;
    
    @Autowired
    ISysUserService sysUserService;
    
    @Autowired
    com.hxw.wscrm.sys.service.ISysMenuService sysMenuService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                // 使用通配符匹配任何上下文路径下的资源
                .antMatchers("/**/doc.html", "/**/doc.html/**",
                        "/**/webjars/**", "/**/v2/**",
                        "/**/v3/**", "/**/swagger-resources/**",
                        "/**/swagger-ui.html", "/**/swagger-ui.html/**").permitAll()
                .antMatchers("/api/*/auth/**","/test/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .addFilter(getTokenLoginFilter())
                .addFilter(getTokenVerifyFilter())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许的源，*表示允许所有源
        configuration.setAllowedOrigins(Arrays.asList("*"));
        // 允许的请求方法
        configuration.setAllowedMethods(Arrays.asList("*"));
        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 预检请求的缓存时间
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径应用CORS配置
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
    
    /**
     * 创建并配置TokenLoginFilter实例
     * @return TokenLoginFilter实例
     * @throws Exception 异常
     */
    private TokenLoginFilter getTokenLoginFilter() throws Exception {
        TokenLoginFilter tokenLoginFilter = new TokenLoginFilter(super.authenticationManager());
        // 设置sysUserService
        tokenLoginFilter.setSysUserService(sysUserService);
        // 设置sysMenuService
        tokenLoginFilter.setSysMenuService(sysMenuService);
        return tokenLoginFilter;
    }
    
    /**
     * 创建并配置TokenVerifyFilter实例
     * @return TokenVerifyFilter实例
     * @throws Exception 异常
     */
    private TokenVerifyFilter getTokenVerifyFilter() throws Exception {
        TokenVerifyFilter tokenVerifyFilter = new TokenVerifyFilter(super.authenticationManager());
        // 设置sysMenuService
        tokenVerifyFilter.setSysMenuService(sysMenuService);
        // 设置sysUserService
        tokenVerifyFilter.setSysUserService(sysUserService);
        return tokenVerifyFilter;
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
