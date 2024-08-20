package com.nvs.config.security.jwt;

import com.nvs.common.Default;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain) throws ServletException, IOException {
    String HEADER_STRING = Default.JWT.HEADER_STRING;
    String authHeader = request.getHeader(HEADER_STRING);
    String token = null;
    String username = null;

    log.debug("-- Processing authentication for request: {}", request.getRequestURI());

    // Extract token
    String TOKEN_PREFIX = Default.JWT.TOKEN_PREFIX;
    if ((authHeader != null) && authHeader.startsWith(TOKEN_PREFIX)) {
      token = authHeader.substring(TOKEN_PREFIX.length());
      username = jwtService.extractUsername(token);
      log.debug("-- Token extracted: {}", token);
      log.debug("-- Username extracted from token: {}", username);
    } else {
      log.warn("-- No valid Authorization header found in request");
    }

    // Validate token and set authentication
    if ((username != null) && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      log.debug("-- UserDetails loaded for username: {}", username);

      if (jwtService.isValidToken(token, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.info("-- Authentication set for user: {}", username);
      } else {
        log.warn("-- Invalid token for username: {}", username);
      }
    } else if (username == null) {
      log.warn("-- Username is null, cannot set authentication");
    } else {
      log.debug("-- Authentication already set in context for user: {}", username);
    }

    filterChain.doFilter(request, response);
  }
}
