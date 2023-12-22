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
public class AuditConfig {
    @Autowired
    private UserRepository userRepository;

    // Maintain a cache for email-to-ID mapping (to avoid loop between AuditorAware and repository)
    private static Map<String, Long> emailToIdCache = new ConcurrentHashMap<>();

    
    @Bean
    public AuditorAware<Long> auditorAware() {//auto set createdBy and lastModifiedBy base on current Authentication
        return () -> {
            try {
                Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
                if (currentAuth != null
                        && currentAuth.isAuthenticated()
                        && !(currentAuth instanceof AnonymousAuthenticationToken)) {
                    String email = currentAuth.getName();
                    Long userId = emailToIdCache.get(email);
                    if (userId != null) {
                        return Optional.ofNullable(userId);
                    } else {
                        return userRepository.findByEmail(email)
                                .map(User::getId)
                                .map(id -> {
                                    emailToIdCache.put(email, id);
                                    return id;
                                });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Optional.empty();
        };
    }

}