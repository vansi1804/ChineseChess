package com.service;

import java.util.List;

import com.data.dto.UserDTO;

public interface UserService {
    List<UserDTO> findAll();

    UserDTO findById(long id);

    UserDTO findByPhoneNumber(String phoneNumber);

    UserDTO findByName(String name);

}
