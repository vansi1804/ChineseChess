package com.nvs.config.audit;

import com.nvs.data.entity.User;
import com.nvs.data.repository.UserRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@AllArgsConstructor
@Slf4j
public class AuditorAwareConfiguration {

  private final UserRepository userRepository;

  @Bean
  public ConcurrentHashMap<String, Long> phoneNumberToIdCache() {
    return new ConcurrentHashMap<>();
  }

  @Bean
  public AuditorAware<Long> auditorAware() {
    return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .filter(this::isAuthenticated).map(Authentication::getName)
        .flatMap(this::getUserIdFromCacheOrRepository);
  }

  private boolean isAuthenticated(Authentication authentication) {
    boolean authenticated = authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken);
    log.debug("Authentication checked: {}, authenticated: {}", authentication.getName(),
        authenticated);
    return authenticated;
  }

  private Optional<Long> getUserIdFromCacheOrRepository(String phoneNumber) {
    log.debug("Attempting to retrieve user ID for phone number: {}", phoneNumber);

    return Optional.ofNullable(phoneNumberToIdCache().get(phoneNumber)).or(() -> {
      log.debug("User ID not found in cache, querying database for phone number: {}", phoneNumber);
      return Optional.ofNullable(fetchUserIdFromRepository(phoneNumber));
    });
  }

  private Long fetchUserIdFromRepository(String phoneNumber) {
    User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    if (user != null) {
      log.info("User found in database: {}, updating cache", user.getId());
      updatePhoneNumberToIdCache(phoneNumber, user.getId());
      return user.getId();
    } else {
      log.warn("No user found in database for phone number: {}", phoneNumber);
    }
    return null;
  }

  @Async
  private void updatePhoneNumberToIdCache(String phoneNumber, Long userId) {
    log.debug("Updating cache with phone number: {} and user ID: {}", phoneNumber, userId);
    phoneNumberToIdCache().put(phoneNumber, userId);
  }
}
