package com.nvs.config.security.userDetails;

import com.nvs.config.exception.UnauthorizedExceptionCustomize;
import com.nvs.data.entity.User;
import com.nvs.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
    User user = userRepository.findByPhoneNumber(phoneNumber)
        .orElseThrow(UnauthorizedExceptionCustomize::new);

    return new UserDetailsImpl(user);
  }
}
