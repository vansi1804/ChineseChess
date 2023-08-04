package com.config.audit;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;

    @Bean
    public AuditorAware<User> auditorAware() {
        return () -> {
            Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
            if (currentAuth != null
                    && currentAuth.isAuthenticated()
                    && !(currentAuth instanceof AnonymousAuthenticationToken)) {

                String phoneNumber = currentAuth.getName();
                return userRepository.findByPhoneNumber(phoneNumber);
            }

            return Optional.empty();
        };
    }

}