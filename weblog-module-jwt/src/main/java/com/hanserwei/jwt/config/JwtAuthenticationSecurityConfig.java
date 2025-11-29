package com.hanserwei.jwt.config;

import com.hanserwei.jwt.filter.JwtAuthenticationFilter;
import com.hanserwei.jwt.handler.RestAuthenticationFailureHandler;
import com.hanserwei.jwt.handler.RestAuthenticationSuccessHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class JwtAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

    @Resource
    private RestAuthenticationFailureHandler restAuthenticationFailureHandler;

    @Override
    public void configure(HttpSecurity httpSecurity) {
        // 1. 实例化自定义过滤器
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();

        // 2. 这里的 AuthenticationManager 是从外部传入的 (httpSecurity sharedObject)
        filter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));

        // 3. 设置成功/失败处理器
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);

        // 4. 将过滤器添加到 UsernamePasswordAuthenticationFilter 之前
        // (JWT 校验通常在用户名密码校验之前，或者用来替换它，addFilterBefore 是最稳妥的)
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
