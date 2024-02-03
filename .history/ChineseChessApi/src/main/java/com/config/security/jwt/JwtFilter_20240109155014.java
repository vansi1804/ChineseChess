package com.config.security.jwt;

import com.common.Default;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final String TOKEN_PREFIX = Default.JWT.TOKEN_PREFIX;
  private final String HEADER_STRING = Default.JWT.HEADER_STRING;

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String authHeader = request.getHeader(HEADER_STRING);
    String token = null;
    String username = null;

    // extract token
    if ((authHeader != null) && authHeader.startsWith(TOKEN_PREFIX)) {
      token = authHeader.substring(TOKEN_PREFIX.length());
      username = jwtService.extractUsername(token);
    }

    if (
      (username != null) &&
      SecurityContextHolder.getContext().getAuthentication() == null
    ) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      if (jwtService.isValidToken(token, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );

        authToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}