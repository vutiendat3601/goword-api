package tech.cdnl.goword.shared.utils;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    @Value("${app.token.scret_key}")
    private String scretKey;

    @Value("${app.token.valid_time_sec}")
    private long tokenValidTimeSec;

    public String generateJwt(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenValidTimeSec * 1000);

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, scretKey)
                .compact();
        return jwtToken;
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(scretKey)
                    .parseClaimsJws(jwtToken);

            Claims claims = claimsJws.getBody();
            Date expiration = claims.getExpiration();
            Date now = new Date();

            return !expiration.before(now);
        } catch (Exception e) {
            return false;
        }
    }

    public Claims decodeJwtToken(String jwtToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(scretKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
}