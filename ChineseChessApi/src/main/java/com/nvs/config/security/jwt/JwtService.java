package com.nvs.config.security.jwt;

import com.nvs.common.Default;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtService {

  public String extractUsername(String token) {
    log.debug("Extracting username from token");
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    log.debug("Extracting expiration date from token");
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    log.debug("Extracting claim from token: {}", claims);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    log.debug("Extracting all claims from token");
    return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    boolean expired = extractExpiration(token).before(new Date());
    log.debug("Checking if token is expired: {}", expired);
    return expired;
  }

  public Boolean isValidToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    boolean isValid = (StringUtils.equals(username, userDetails.getUsername()) && !isTokenExpired(
        token));
    log.info("Token validation result for user '{}': {}", username, isValid);
    return isValid;
  }

  public String generateToken(String userName, long expirationTime) {
    log.debug("Generating token for user: {}", userName);
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userName, expirationTime);
  }

  private String createToken(Map<String, Object> claims, String userName, long expirationTime) {
    final Date createdDate = new Date();
    final Date expiredDate = new Date(createdDate.getTime() + expirationTime);
    String token = Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(createdDate)
        .setExpiration(expiredDate).signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    log.info("Token created for user '{}', expires at: {}", userName, expiredDate);
    return token;
  }

  private Key getSignKey() {
    log.debug("Retrieving signing key");
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(Default.JWT.SECRET));
  }
}
