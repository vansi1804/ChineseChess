package com.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.ERole;
import com.common.enumeration.EStatus;
import com.data.dto.UserCreationDTO;
import com.data.dto.UserDTO;
import com.data.dto.UserProfileDTO;
import com.data.entity.User;
import com.exception.ConflictException;
import com.exception.ResourceNotFoundException;
import com.data.mapper.UserMapper;
import com.data.repository.RoleRepository;
import com.data.repository.UserRepository;
import com.data.repository.VipRepository;
import com.service.UserService;
import com.util.Encoding;

@Service
public class UserServiceImpl implements UserService {
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private UserMapper userMapper;
        @Autowired
        private RoleRepository roleRepository;
        @Autowired
        private VipRepository vipRepository;

        @Override
        public Page<UserDTO> findAll(int no, int limit, String sortBy) {
                return userRepository.findAll(PageRequest.of(no, limit, Sort.by(sortBy)))
                                .map(u -> userMapper.toDTO(u));
        }

        @Override
        public UserDTO findById(long id) {
                return userRepository.findById(id)
                                .map(u -> userMapper.toDTO(u))
                                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        }

        @Override
        public UserDTO findByPhoneNumber(String phoneNumber) {
                return userRepository.findByPhoneNumber(phoneNumber)
                                .map(u -> userMapper.toDTO(u))
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                Collections.singletonMap("phoneNumber", phoneNumber)));

        }

        @Override
        public UserDTO findByName(String name) {
                return userRepository.findByName(name)
                                .map(u -> userMapper.toDTO(u))
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                Collections.singletonMap("name", name)));

        }

        @Override
        public UserDTO create(UserCreationDTO userCreationDTO, ERole eRole) {
                if (userRepository.existsByPhoneNumber(userCreationDTO.getPhoneNumber())) {
                        throw new ConflictException(
                                        Collections.singletonMap("phoneNumber", userCreationDTO.getPhoneNumber()));
                }

                User createUser = userMapper.toEntity(userCreationDTO);
                try {
                        createUser.setPassword(Encoding.getMD5(userCreationDTO.getPassword()));
                } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                }
                createUser.setRole(roleRepository.findByName(eRole.name())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                Collections.singletonMap("Role name", eRole.name()))));
                String vipDefault = Default.VIP.name();
                createUser.setVip(vipRepository.findByName(vipDefault)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                Collections.singletonMap("Vip name", eRole.name()))));
                createUser.setStatus(Default.STATUS.name());

                return userMapper.toDTO(userRepository.save(createUser));
        }

        @Override
        public UserProfileDTO update(long id, UserProfileDTO userProfileDTO) {
                User oldUser = userRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

                if (userRepository.existsByIdNotAndPhoneNumber(id, userProfileDTO.getPhoneNumber())) {
                        throw new ResourceNotFoundException(
                                        Collections.singletonMap("phoneNumber", userProfileDTO.getPhoneNumber()));
                }

                User updateUser = userMapper.toEntity(userProfileDTO);
                updateUser.setId(oldUser.getId());
                updateUser.setPassword(oldUser.getPassword());
                updateUser.setRole(oldUser.getRole());
                updateUser.setVip(oldUser.getVip());
                updateUser.setStatus(oldUser.getStatus());

                return userMapper.toProfileDTO(userRepository.save(updateUser));
        }

        @Override
        public UserDTO updateStatusById(long id, EStatus eStatus) {
                User user = userRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

                user.setStatus(eStatus.name());
                return userMapper.toDTO(userRepository.save(user));
        }

}
