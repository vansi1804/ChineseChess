package com.nvs.config.audit;

import com.nvs.data.entity.User;
import com.nvs.data.repository.UserRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@AllArgsConstructor
public class AuditorAwareConfiguration {

  private final UserRepository userRepository;

  @Bean
  public ConcurrentHashMap<String, Long> phoneNumberToIdCache() {
    return new ConcurrentHashMap<>();
  }

  @Bean
  public AuditorAware<Long> auditorAware(ConcurrentHashMap<String, Long> phoneNumberToIdCache) {
    return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .filter(this::isAuthenticated).map(Authentication::getName)
        .flatMap(phoneNumber -> getUserIdFromCacheOrRepository(phoneNumber, phoneNumberToIdCache));
  }

  private boolean isAuthenticated(Authentication authentication) {
    return authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken);
  }

  private Optional<Long> getUserIdFromCacheOrRepository(String phoneNumber,
      ConcurrentHashMap<String, Long> phoneNumberToIdCache) {
    return Optional.ofNullable(
        phoneNumberToIdCache.computeIfAbsent(phoneNumber, this::fetchUserIdFromRepository));
  }

  private Long fetchUserIdFromRepository(String phoneNumber) {
    return userRepository.findByPhoneNumber(phoneNumber).map(User::getId).orElse(null);
  }

}
