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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@AllArgsConstructor
public class AuditorAwareConfiguration {

  private final UserRepository userRepository; // Repository for user operations

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

  // Checks if the authentication is valid and not anonymous
  private boolean isAuthenticated(Authentication authentication) {
    return authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken);
  }

  // Retrieves the user ID from the cache or database
  private Optional<Long> getUserIdFromCacheOrRepository(String phoneNumber) {
    // Try to find the user ID in the cache
    return Optional.ofNullable(phoneNumberToIdCache().get(phoneNumber))
        // If not found in cache, fetch from database
        .or(() -> Optional.ofNullable(fetchUserIdFromRepository(phoneNumber)));
  }

  // Fetches the user ID from the database and updates the cache asynchronously
  private Long fetchUserIdFromRepository(String phoneNumber) {
    User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    if (user != null) {
      // Asynchronously update the cache to avoid recursive update
      updatePhoneNumberToIdCache(phoneNumber, user.getId());
      return user.getId();
    }
    return null;
  }

  // Asynchronously updates the cache with the user ID
  @Async
  private void updatePhoneNumberToIdCache(String phoneNumber, Long userId) {
    phoneNumberToIdCache().put(phoneNumber, userId);
  }
}
