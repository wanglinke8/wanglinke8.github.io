package com.nsc.ipfind.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // 从配置文件读取密钥和过期时间（推荐）
    @Value("${jwt.secret:MySecretKey_1234567890!@#$%^&*()}")
    private String secret;

    @Value("${jwt.expiration:86400}") // 默认24小时，单位：秒
    private Long expiration;

    /**
     * 生成 Token (使用 zhanghao 作为 Subject)
     * 修改点1: 方法参数名从 username 改为 zhanghao
     */
    public String generateToken(String zhanghao) { // <--- 修改这里
        Map<String, Object> claims = new HashMap<>();
        // 修改点2: 调用 createToken 时也使用 zhanghao
        return createToken(claims, zhanghao); // <--- 修改这里
    }

    /**
     * 支持添加自定义声明（如 userId, role 等）
     * 注意：这个方法的 subject 参数可以是任意字符串，不一定非得是 zhanghao
     */
    public String generateTokenWithClaims(Map<String, Object> claims, String subject) {
        return createToken(claims, subject);
    }

    /**
     * 实际创建 Token 的方法
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // Subject 字段存储传入的值 (通常是 zhanghao)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从 Token 中提取用户名（subject）
     * 修改点3: 方法名从 getUsernameFromToken 改为 getZhanghaoFromToken 更准确
     */
    public String getZhanghaoFromToken(String token) { // <--- 修改这里 (方法名)
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 如果你不想改方法名，下面这个保留也可以，但需要清楚它返回的是 zhanghao
    // public String getUsernameFromToken(String token) {
    //     return getClaimFromToken(token, Claims::getSubject);
    // }

    /**
     * 从 Token 中提取某个 Claim（可扩展）
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析 Token 获取所有 Claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断 Token 是否过期
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 获取 Token 的过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 验证 Token 是否有效（签名正确 + 未过期 + Subject 匹配）
     * 修改点4: 参数名和内部逻辑改为 zhanghao
     */
    public Boolean validateToken(String token, String zhanghao) { // <--- 修改这里 (参数名)
        // 修改点5: 调用 getZhanghaoFromToken (如果你改了方法名的话)
        final String tokenZhanghao = getZhanghaoFromToken(token); // <--- 修改这里
        return (tokenZhanghao != null && tokenZhanghao.equals(zhanghao) && !isTokenExpired(token)); // <--- 修改这里 (变量名)
    }

    /**
     * 仅验证 Token 是否有效（不校验用户名/zhanghao）
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
