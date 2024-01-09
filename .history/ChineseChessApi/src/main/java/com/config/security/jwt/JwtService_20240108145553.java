package com.config.security.jwt;

import com.common.Default;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);

    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
      .parserBuilder()
      .setSigningKey(getSignKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean isValidToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);

    return (
      StringUtils.equals(username, userDetails.getUsername()) &&
      !isTokenExpired(token)
    );
  }

  public String generateToken(String userName, long expirationTime) {
    Map<String, Object> claims = new HashMap<>();

    return createToken(claims, userName,expirationTime);
  }

  private String createToken(Map<String, Object> claims, String userName, long ) {
    final Date createdDate = new Date();
    final Date expiredDate = new Date(
      createdDate.getTime() + Default.JWT.ACCESS_TOKEN_EXPIRATION_TIME
    );

    return Jwts
      .builder()
      .setClaims(claims)
      .setSubject(userName)
      .setIssuedAt(createdDate)
      .setExpiration(expiredDate)
      .signWith(getSignKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  private Key getSignKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(Default.JWT.SECRET));
  }
}
