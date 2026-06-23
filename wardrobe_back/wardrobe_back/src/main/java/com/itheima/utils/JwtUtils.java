package com.itheima.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

public class JwtUtils {
    private static final String SECRET = "wardrobe_secret_key_2024";
    private static final long EXPIRE_HOURS = 24;

    // 生成 token，把 userId 写入 audience
    public static String createToken(int userId) {
        return JWT.create()
                .withAudience(String.valueOf(userId))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_HOURS * 3600 * 1000))
                .sign(Algorithm.HMAC256(SECRET));
    }
}
