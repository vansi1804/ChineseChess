package com.config.audit;

import com.data.entity.User;
import com.data.repository.UserRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomAuditorAware implements AuditorAware<Long> {

    private final UserRepository userRepository;
    private final ConcurrentHashMap<String, Long> phoneNumberToIdCache = new ConcurrentHashMap<>();

    public CustomAuditorAware(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            Authentication currentAuth = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            if (currentAuth != null &&
                    currentAuth.isAuthenticated() &&
                    !(currentAuth instanceof AnonymousAuthenticationToken)) {
                String phoneNumber = currentAuth.getName();
                return getUserIdFromCacheOrRepository(phoneNumber);
            }
        } catch (Exception e) {
            // Handle specific exceptions you expect might occur
            // Log the exception or handle it according to your application's needs
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private synchronized Optional<Long> getUserIdFromCacheOrRepository(String phoneNumber) {
        Long userId = phoneNumberToIdCache.get(phoneNumber);
        if (userId != null) {
            return Optional.of(userId);
        } else {
            return userRepository
                    .findByPhoneNumber(phoneNumber)
                    .map(User::getId)
                    .map(id -> {
                        phoneNumberToIdCache.put(phoneNumber, id);
                        return id;
                    });
        }
    }
}
