package com.hxw.wscrm.common.result;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    private static final String SECRET = "xiaohe";

    /**
     * 生成Token信息
     * @param map
     * @return
     */
    public static String getToken(Map<String,String> map) {
        JWTCreator.Builder builder = JWT.create();
        //设置payload
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        //设置过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,15); //默认过期时间15天
        Map<String,Object> head = new HashMap<>();
        head.put("alg","HS256");
        head.put("typ","JWT");
        return builder.withHeader(head).withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 校验这个Token是否合法
     *      如果非法抛出异常信息
     *      如果合法就返回DecodedJWT对象
     * @param token
     * @return
     */
    public static DecodedJWT verify(String token) {
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        }catch (SignatureVerificationException e){
            System.out.println("算法不匹配异常：" + e.getMessage());
        }catch (AlgorithmMismatchException e){
            System.out.println("签名验证异常：" + e.getMessage());
        }catch (Exception e){
            System.out.println("其他异常：" + e.getMessage());
        }
        return decodedJWT;
    }
}
