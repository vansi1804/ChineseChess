package com.config.security.userDetails;

import com.config.exception.UnauthorizedExceptionCustomize;
import com.data.entity.User;
import com.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String phoneNumber)
    throws UsernameNotFoundException {
    User user = userRepository
      .findByPhoneNumber(phoneNumber)
      .orElseThrow(() -> new UnauthorizedExceptionCustomize());

    return new UserDetailsImpl(user);
  }
}
