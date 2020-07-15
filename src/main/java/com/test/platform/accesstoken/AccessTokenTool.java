package com.test.platform.accesstoken;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AccessTokenTool {
	
	/**获取access_token 封装business信息及过期时间 */
	public static String getAccessTokenByBusinessAndExpireTime(String serviceSecret, AccessTokenInfo accessTokenInfo, Long expire) {
		 //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(accessTokenInfo.getClaims())
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(accessTokenInfo.getId())
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, serviceSecret);
        if (expire >= 0) {
            long expMillis = nowMillis + expire;
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
	}
	
	/**通过access_token校验和解析出业务信息*/
	public static Map<String, Object> checkAndgetBusinessByAccessToken(String serviceSecret, String accessToken) {

        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(serviceSecret)
                //设置需要解析的jwt
                .parseClaimsJws(accessToken).getBody();
        return claims;
	}
}
