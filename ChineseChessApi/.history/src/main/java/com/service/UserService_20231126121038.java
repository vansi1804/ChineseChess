package com.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.common.enumeration.ERole;
import com.data.dto.user.UserCreationDTO;
import com.data.dto.user.UserDTO;
import com.data.dto.user.UserProfileDTO;

public interface UserService {

    Page<UserDTO> findAll(int no, int limit, String sortBy);

    UserDTO findById(long id);

    UserDTO findByPhoneNumber(String phoneNumber);

    UserDTO findByName(String name);

    UserDTO create(UserCreationDTO userCreationDTO, ERole eRole);

    UserProfileDTO update(long id, UserProfileDTO userProfileDTO);

    @Transactional
    boolean lockById(long id);
    
    @Transactional
    boolean unlockById(long id);

    boolean isCurrentUser(long id);

}
