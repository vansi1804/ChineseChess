package com.config.audit;

public class AuditorAwareCustom {
      @Autowired
    private UserRepository userRepository;

    @Bean
    public AuditorAware<Long> auditorAware() {
        return new CustomAuditorAware(userRepository);
    }
}
