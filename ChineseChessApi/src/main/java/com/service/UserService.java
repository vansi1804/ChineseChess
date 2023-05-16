package com.service;


import org.springframework.data.domain.Page;

import com.common.enumeration.ERole;
import com.common.enumeration.EStatus;
import com.data.dto.UserCreationDTO;
import com.data.dto.UserDTO;
import com.data.dto.UserProfileDTO;

public interface UserService {
    Page<UserDTO> findAll(int no, int limit, String sortBy);

    UserDTO findById(long id);

    UserDTO findByPhoneNumber(String phoneNumber);

    UserDTO findByName(String name);

    UserDTO create(UserCreationDTO userCreationDTO, ERole eRole);

    UserProfileDTO update(long id, UserProfileDTO userProfileDTO);

    UserDTO updateStatusById(long id, EStatus eStatus);
}
