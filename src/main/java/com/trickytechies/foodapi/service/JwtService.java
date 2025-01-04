package com.trickytechies.foodapi.service;

import java.util.Date;
import java.util.function.Function;
import java.util.Base64.Decoder;

import javax.crypto.SecretKey;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.trickytechies.foodapi.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private String SECRERT_KEY = "e40bbcdbdaadfb54276af9e528798fccfc0dba9c89123ca332f1545331db88c82b3fa40b0de947d59b449f9306569cf85068cc56d9ebc2adc539c26410b2ca0ac057d68b05c45e38db34a08413a00513fca3f08c58e7816364d1117a4b1f5c98b43d249f2d53ebfa5eb959ee74e9f50a56d29372b2798e327287c4ee003446031621ac659876d271694d31295b4f2df9c1afcc170e943bcad32ab0e14b750e21806efa3eaaa766629d5e57884f0c406a18cd1c008fdf9b3431ebcb88a1b674c4caf2c4c9313dc0fb7d2057a2e5492b32d8845ace6e93624acfab9c63448f5e89df9b736f00d6d7953631a0600847cefa4e69032e4b05a8558902a90dc73";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
        .parser()
        .verifyWith(getSigninKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }



    public String generateToken(User user) {
        String token=Jwts
        .builder()
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1))
        .signWith(getSigninKey())
        .compact();  
        return token;
    }


    private SecretKey getSigninKey() {
        byte[] bytes = Decoders.BASE64URL.decode(SECRERT_KEY);
        return Keys.hmacShaKeyFor(bytes); 
    }

}
