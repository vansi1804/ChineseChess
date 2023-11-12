package com.config.audit;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.data.entity.User;
import com.data.repository.UserRepository;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditorAwareConfiguration {

    private final UserRepository userRepository;
    
    // Maintain a cache for email-to-ID mapping (to avoid loop between AuditorAware and repository)
    private static Map<String, Long> phoneNumberToIdCache = new ConcurrentHashMap<>();

    public AuditorAwareConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Bean
    public AuditorAware<Long> auditorAware() {// auto set createdBy and lastModifiedBy base on current Authentication
        return () -> {
            try {
                Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
                if (currentAuth != null
                        && currentAuth.isAuthenticated()
                        && !(currentAuth instanceof AnonymousAuthenticationToken)) {
                    
                    String phoneNumber = currentAuth.getName();
                    Long userId = phoneNumberToIdCache.get(phoneNumber);
                    if (userId != null) {
                        return Optional.ofNullable(userId);
                    } else {
                        return userRepository.findByPhoneNumber(phoneNumber)
                                .map(User::getId)
                                .map(id -> {
                                    phoneNumberToIdCache.put(phoneNumber, id);
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