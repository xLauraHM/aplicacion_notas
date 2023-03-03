package com.laura.notas.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class TokenUtils {
    private final static String ACCESS_TOKEN_SECRET = "QWENASIOUD23MN4089ASDF2349ASJKD239084A890KVKSDJLF90239084";
    private final static long ACCESS_VALIDITY_SECONDS = 120;

    public static String createToken(String nombre, String username) {

        long expirationTimeInMillis = ACCESS_VALIDITY_SECONDS * 1000;
        long currentTimeInMillis = GregorianCalendar.getInstance().getTimeInMillis();

        Date expirationDate = new Date(currentTimeInMillis + expirationTimeInMillis);

        Map<String, Object> extra = new HashMap<String, Object>();
        extra.put("nombre", nombre);

        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();

        return token;
    }

    public static UsernamePasswordAuthenticationToken gAuthenticationToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
    }
}
