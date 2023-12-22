package com.config.audit;

import com.data.entity.User;
import com.data.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@RequiredArgsConstructor
public class AuditorAwareConfiguration implements AuditorAware<Long> {

  private final UserRepository userRepository;

  
  @Override
  public Optional<Long> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder
      .getContext()
      .getAuthentication();
    if (
      authentication != null &&
      authentication.isAuthenticated() &&
      !(authentication instanceof AnonymousAuthenticationToken)
    ) {
      String phoneNumber = authentication.getName();
      Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
      return user.map(User::getId);
    }
    return Optional.empty();
  }
}
