package com.nvs.service;

import com.nvs.common.enumeration.ERole;
import com.nvs.data.dto.user.UserChangePasswordRequestDTO;
import com.nvs.data.dto.user.UserCreationDTO;
import com.nvs.data.dto.user.UserDTO;
import com.nvs.data.dto.user.UserProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface UserService{

   Page<UserDTO> findAll(int no, int limit, String sortBy);

   UserDTO findById(long id);

   UserDTO findByPhoneNumber(String phoneNumber);

   UserDTO findByName(String name);

   UserDTO create(UserCreationDTO userCreationDTO, ERole eRole);

   UserProfileDTO update(long id, UserProfileDTO userProfileDTO);

   @Transactional
   UserDTO lockById(long id);

   @Transactional
   UserDTO unlockById(long id);

   boolean isCurrentUser(long id);

   UserProfileDTO changePassword(long id, UserChangePasswordRequestDTO userChangePasswordRequestDTO);

}
