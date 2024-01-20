package com.config.audit;

import org.springframework.data.domain.AuditorAware;

public interface AuditorAwareConfiguration {

      AuditorAware<Long> auditorAware();

}