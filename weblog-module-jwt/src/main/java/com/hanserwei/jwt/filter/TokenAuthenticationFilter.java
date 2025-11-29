package com.hanserwei.jwt.filter;

import com.hanserwei.jwt.utils.JwtTokenHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private JwtTokenHelper jwtTokenHelper;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        // 1. 从请求头中获取 Authorization
        String header = request.getHeader("Authorization");

        // 2. 校验头格式 (必须以 Bearer 开头)
        if (StringUtils.startsWith(header, "Bearer ")) {
            String token = StringUtils.substring(header, 7);
            log.info("JWT Token: {}", token);
            if (StringUtils.isNotBlank(token)) {
                try {
                    // 3. 解析 Token (核心步骤)
                    // 注意：JwtTokenHelper.parseToken 方法内部已经处理了 JWT 格式校验和过期校验，
                    // 并将 JJWT 异常转换为了 Spring Security 的 AuthenticationException 抛出。
                    Jws<Claims> claimsJws = jwtTokenHelper.parseToken(token);

                    // 4. 获取用户名
                    // JJWT 0.12+ 建议使用 getPayload() 替代 getBody()
                    // 在 Helper 中生成 Token 时使用的是 .subject(username)，所以这里取 Subject
                    String username = claimsJws.getPayload().getSubject();

                    // 5. 组装认证信息 (如果当前上下文没有认证信息)
                    if (StringUtils.isNotBlank(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {

                        // 查询数据库获取用户完整信息 (包含权限)
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        // 构建 Spring Security 的认证 Token
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 6. 将认证信息存入 SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (AuthenticationException e) {
                    // 7. 异常处理
                    // 捕获 JwtTokenHelper 抛出的 BadCredentialsException 或 CredentialsExpiredException
                    // 如果 Token 存在但是无效/过期，直接交给 EntryPoint 处理响应 (通常返回 401)
                    // 并 return 结束当前过滤器，不再往下执行
                    authenticationEntryPoint.commence(request, response, e);
                    return;
                } catch (Exception e) {
                    // 处理其他未预料到的异常
                    log.error("JWT处理过程中发生未知错误", e);
                    authenticationEntryPoint.commence(request, response, new AuthenticationException("系统内部认证错误") {
                    });
                    return;
                }
            }
        }

        // 8. 放行请求 (如果没有 Token 或者 Token 校验通过，继续执行下一个过滤器)
        filterChain.doFilter(request, response);
    }
}