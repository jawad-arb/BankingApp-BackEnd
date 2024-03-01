package org.arbahi.banking_application.configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;



@Component
public class JwtTokenProvider {
    @Value("${app.tokenKey}")
    private String jwtSecret;
    @Value("${app.tokenDateLine}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication){
        String username=authentication.name();
        Date currentDate=new Date();
        Date expiredDate=new Date(currentDate.getTime() + jwtExpirationDate);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiredDate)
                .signWith(key())
                .compact();
    }

    private Key key(){
        byte[] bytes= Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }


}
