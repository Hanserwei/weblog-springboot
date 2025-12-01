package com.hanserwei.jwt.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenHelper implements InitializingBean {

    /**
     * 签发人
     */
    @Value("${jwt.issuer}")
    private String issuer;

    /**
     * 秘钥 (JJWT 0.12+ 强制要求使用 SecretKey 接口，且长度必须符合算法安全标准)
     */
    private SecretKey key;

    /**
     * JWT 解析器 (线程安全，建议复用)
     */
    private JwtParser jwtParser;

    /**
     * 工具方法：生成一个符合 HS512 标准的安全 Base64 秘钥
     * 用于生成后填入 application.yml
     */
    public static String generateBase64Key() {
        SecretKey secretKey = Jwts.SIG.HS512.key().build();
        return Encoders.BASE64.encode(secretKey.getEncoded()); // Fixed line
    }

    public static void main(String[] args) {
        String key = generateBase64Key();
        System.out.println("请将此 Key 复制到配置文件中 (HS512需要长Key): " + key);
    }

    /**
     * 设置 Base64 秘钥
     * 官方建议：HS512 算法至少需要 512 bit (64字节) 的秘钥长度，否则会抛出 WeakKeyException
     */
    @Value("${jwt.secret}")
    public void setBase64Key(String base64Key) {
        // 使用 JJWT 提供的 Decoders 工具，或者使用 java.util.Base64 均可
        byte[] keyBytes = Decoders.BASE64.decode(base64Key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 初始化 JwtParser
     */
    @Override
    public void afterPropertiesSet() {
        // 0.13.0 标准写法：
        // 1. 使用 parser() 而不是 parserBuilder()
        // 2. 使用 verifyWith() 设置验签秘钥
        jwtParser = Jwts.parser()
                .requireIssuer(issuer)
                .verifyWith(key)
                // 允许的时钟偏差 (防止服务器时间不一致导致验证失败)，默认单位秒
                .clockSkewSeconds(10)
                .build();
    }

    /**
     * 生成 Token
     */
    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expireTime = now.plus(30, ChronoUnit.DAYS);

        return Jwts.builder()
                .header().add("type", "JWT").and() // 推荐添加 header
                .subject(username)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireTime))
                // 0.13.0 标准：使用 Jwts.SIG 中的常量指定算法
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    /**
     * 解析 Token
     */
    public Jws<Claims> parseToken(String token) {
        try {
            // 0.13.0 标准：解析已签名的 JWS 使用 parseSignedClaims
            return jwtParser.parseSignedClaims(token);
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            // 注意：SignatureException 现在属于 io.jsonwebtoken.security 包
            throw new BadCredentialsException("Token 不可用或签名无效", e);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("Token 已失效", e);
        }
    }
}