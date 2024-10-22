package com.example.api_gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtUtils {
    private final String secretKey = "hagsjkalsfatuyovooauwncjsmdlaisytqopdofuankjsgbxttnkwsik6738659asndu72dh289498isdf234ds4";

    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isExpired(String token){
        try{
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Integer extractUserId(String token){
        try{
            return Integer.parseInt(getClaims(token).getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}
