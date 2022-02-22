package jedrekp.daycarecateringbillgenerator.security;

import io.jsonwebtoken.*;
import jedrekp.daycarecateringbillgenerator.entity.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenUtil {

    @Value("${jwt.signing.key.secret}")
    private String secret;

    @Value("${jwt.token.expiration.in.seconds}")
    private Long expiration;

    String generateToken(AppUser appUser) {
        Map<String, Object> claims = new HashMap<>();
        Date createdDate = new Date();
        Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(appUser.getUsername())
                .claim("fullName", MessageFormat.format("{0} {1}", appUser.getFirstName(), appUser.getLastName()))
                .claim("userRole", appUser.getDaycareRole().toString())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    Principal decodeToken(String token) {
        if (isTokenValid(token)) {
            String username = getUsernameFromToken(token);
            String userRole = getUserRoleFromToken(token);
            return new Principal(username, userRole);
        }
        return null;
    }

    private boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT format: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        }
        return false;
    }


    private String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    private String getUserRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("userRole", String.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }
}


