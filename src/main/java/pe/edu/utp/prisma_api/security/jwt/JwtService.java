package pe.edu.utp.prisma_api.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import pe.edu.utp.prisma_api.domain.user.User;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private long expiration;

  public String generateToken(User user) {
    return Jwts.builder()
        .subject(user.getId())
        .claim("email", user.getEmail())
        .claim("role", user.getRole().name())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getKey())
        .compact();
  }

  public String extractUserId(String token) {
    return getClaims(token).getSubject();
  }

  public String extractEmail(String token) {
    return getClaims(token).get("email", String.class);
  }

  public boolean isValid(String token) {
    System.out.println("------------------");
    System.out.println(token);
    System.out.println("------------------");
    try {
      getClaims(token);
      return true;
    } catch (Exception e) {
      System.out.println("------------------");
      System.out.println(e.getMessage());
      System.out.println("------------------");
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  public long getJwtExpiration() {
    return expiration;
  }
}