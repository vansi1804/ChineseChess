package com.config.audit;

import com.data.entity.User;
import com.data.repository.UserRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditorAwareConfiguration {

  @Autowired
  private UserRepository userRepository;

  // Ensure thread-safe access for phoneNumber-to-ID mapping
  private final Map<String, Long> phoneNumberToIdCache = new ConcurrentHashMap<>();

  @Bean
  public AuditorAware<Long> auditorAware() {
    return () -> {
      try {
        Authentication currentAuth = SecurityContextHolder
          .getContext()
          .getAuthentication();

        if (
          currentAuth != null &&
          currentAuth.isAuthenticated() &&
          !(currentAuth instanceof AnonymousAuthenticationToken)
        ) {
          String phoneNumber = currentAuth.getName();
          return Optional.ofNullable(
            getUserIdFromCacheOrRepository(phoneNumber)
          );
        }
      } catch (Exception e) {
        // Handle specific exceptions you expect might occur
        // Log the exception or handle it according to your application's needs
        e.printStackTrace();
      }
      return Optional.empty();
    };
  }

  // Synchronized method to get UserID either from Cache or Repository
  private synchronized Long getUserIdFromCacheOrRepository(String phoneNumber) {
    Long userId = phoneNumberToIdCache.get(phoneNumber);
    if (userId != null) {
      return userId;
    } else {
      return userRepository
        .findByPhoneNumber(phoneNumber)
        .map(User::getId)
        .map(id -> {
          phoneNumberToIdCache.put(phoneNumber, id);
          return id;
        })
        .orElse(null);
    }
  }
}
