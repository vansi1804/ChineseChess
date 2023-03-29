package com.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.ErrorMessage;
import com.data.dto.UserDTO;
import com.exception.ExceptionCustom;
import com.data.mapper.UserMapper;
import com.data.repository.UserRepository;
import com.service.UserService;

@Service
public class UserServiceImpl implements UserService {
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private UserMapper userMapper;

        @Override
        public List<UserDTO> findAll() {
                return userRepository.findAll().stream().map(u -> userMapper.toDTO(u)).collect(Collectors.toList());
        }

        @Override
        public UserDTO findById(long id) {
                return userRepository.findById(id).map(u -> userMapper.toDTO(u))
                                .orElseThrow(() -> new ExceptionCustom("User", ErrorMessage.DATA_NOT_FOUND, "id", id));
        }

        @Override
        public UserDTO findByPhoneNumber(String phoneNumber) {
                return userRepository.findByPhoneNumber(phoneNumber).map(u -> userMapper.toDTO(u))
                                .orElseThrow(() -> new ExceptionCustom(
                                                "User", ErrorMessage.DATA_NOT_FOUND, "phone number", phoneNumber));
        }

        @Override
        public UserDTO findByName(String name) {
                return userRepository.findByName(name).map(u -> userMapper.toDTO(u))
                                .orElseThrow(() -> new ExceptionCustom(
                                                "User", ErrorMessage.DATA_NOT_FOUND, "name", name));
        }

}
