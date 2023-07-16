package com.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.common.enumeration.ERole;
import com.data.dto.user.UserCreationDTO;
import com.data.dto.user.UserDTO;
import com.data.dto.user.UserProfileDTO;

public interface UserService {

    Page<UserDTO> findAll(int no, int limit, String sortBy);

    UserDTO findById(long id);

    UserDTO findByPhoneNumber(String phoneNumber);

    UserDTO findByName(String name);

    UserDTO create(UserCreationDTO userCreationDTO, MultipartFile fileAvatar, ERole eRole);

    UserProfileDTO update(long id, UserProfileDTO userProfileDTO, MultipartFile fileAvatar);

    boolean lockById(long id);

    boolean unlockById(long id);

    boolean isCurrentUser(long id);

}
