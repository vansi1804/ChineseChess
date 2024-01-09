package com.config.audit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.data.repository.UserRepository;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditorAwareConfiguration {

    @Bean
    public AuditorAware<Long> auditorProvider(UserRepository userRepository) {
        return new CustomAuditorAware(userRepository);
    }
}
