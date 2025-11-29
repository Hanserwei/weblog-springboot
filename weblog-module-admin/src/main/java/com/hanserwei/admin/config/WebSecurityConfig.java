package com.hanserwei.admin.config;

import com.hanserwei.jwt.config.JwtAuthenticationSecurityConfig;
import com.hanserwei.jwt.handler.RestAccessDeniedHandler;
import com.hanserwei.jwt.handler.RestAuthenticationEntryPoint;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    @Resource
    private JwtAuthenticationSecurityConfig jwtAuthenticationSecurityConfig;

    @Resource
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 2. 禁用表单登录
                .formLogin(AbstractHttpConfigurer::disable)
                // 3. 授权规则配置
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").authenticated() // 保护 /admin/**
                        .anyRequest().permitAll() // 其他放行
                )
                // 4. 会话管理：无状态
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 5. 自定义未登录/权限不足的响应
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler)
                )
                // 6. 应用自定义配置 (核心变化)
                .with(jwtAuthenticationSecurityConfig, Customizer.withDefaults());
        return http.build();
    }

    /**
     * 定义 AuthenticationProvider Bean。
     * Spring Security 会自动将此 Provider 注入到 AuthenticationManager 中。
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {
        // 修正点：直接在构造函数中传入 userDetailsService
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

        // 设置密码编码器 (PasswordEncoder 依然使用 setter 设置)
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }
}
