package org.phenomenal.toolkit.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class Auth {
    private static final String TOKEN_SECRET = "privateKey";
    private static final long EXPIRE_TIME = 30 * 60 * 1000;
    private static final Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
    public static String genToken(String userId) {
        try {
            return JWT.create()
                    .withClaim("userId", userId)
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String verifyToken(String token){
        try{
            JWTVerifier build = JWT.require(algorithm).build();
            DecodedJWT verify = build.verify(token);
            String userId = verify.getClaim("userId").asString();
            return userId;
        }catch (Exception e){
            return null;
        }
    }
}
