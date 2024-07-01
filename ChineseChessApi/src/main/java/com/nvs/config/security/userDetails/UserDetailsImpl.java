package com.nvs.config.security.userDetails;

import com.nvs.common.enumeration.EStatus;
import com.nvs.data.entity.User;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserDetailsImpl implements UserDetails{

   private final User user;

   public UserDetailsImpl(User user){
      this.user = user;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities(){
      return Collections.singleton(new SimpleGrantedAuthority(user.getRole()
                                                                  .getName()));
   }

   @Override
   public String getPassword(){
      return user.getPassword();
   }

   @Override
   public String getUsername(){
      return user.getPhoneNumber();
   }

   @Override
   public boolean isAccountNonExpired(){
      return true;
   }

   @Override
   public boolean isAccountNonLocked(){
      return !EStatus.LOCK.name()
                          .equals(user.getStatus());
   }

   @Override
   public boolean isCredentialsNonExpired(){
      return true;
   }

   @Override
   public boolean isEnabled(){
      return !EStatus.INACTIVE.name()
                              .equals(user.getStatus());
   }

}
