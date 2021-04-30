package com.lsy.security.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {

    //token有效时长,
    private long tokenEcpiration = 24*60*60*1000;
    //编码秘钥,在实际的项目中是要生成的，这里进行写死。
    private String tokenSignKey = "123456";


    //1 使用jwt根据用户名生成token
    public String createToken(String username) {
        //传入的是用户名
        String token = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis()+tokenEcpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        //加密过程的固定写法
        return token;
    }


    //2 根据token字符串得到用户信息
    public String getUserInfoFromToken(String token) {
        String userinfo = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return userinfo;
    }


    //3 删除token
    public void removeToken(String token) {
        //可以不写删除Token的方法，因为可以在客户端上不携带Token。
    }
}
