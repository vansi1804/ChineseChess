package com.service.impl;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.common.Default;
import com.common.enumeration.ERole;
import com.common.enumeration.EStatus;
import com.data.dto.UserCreationDTO;
import com.data.dto.UserDTO;
import com.data.dto.UserProfileDTO;
import com.data.entity.Role;
import com.data.entity.User;
import com.data.entity.Vip;
import com.config.exception.ConflictException;
import com.config.exception.InternalServerErrorException;
import com.config.exception.ResourceNotFoundException;
import com.data.mapper.UserMapper;
import com.data.repository.RoleRepository;
import com.data.repository.UserRepository;
import com.data.repository.VipRepository;
import com.service.FileService;
import com.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final VipRepository vipRepository;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
            UserMapper userMapper,
            RoleRepository roleRepository,
            VipRepository vipRepository,
            FileService fileService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.vipRepository = vipRepository;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<UserDTO> findAll(int no, int limit, String sortBy) {
        return userRepository.findAll(PageRequest.of(no, limit, Sort.by(sortBy))).map(u -> userMapper.toDTO(u));
    }

    @Override
    public UserDTO findById(long id) {
        return userRepository.findById(id).map(u -> userMapper.toDTO(u))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

    }

    @Override
    public UserDTO findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).map(u -> userMapper.toDTO(u))
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("phoneNumber", phoneNumber)));

    }

    @Override
    public UserDTO findByName(String name) {
        return userRepository.findByName(name).map(u -> userMapper.toDTO(u))
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("name", name)));

    }

    @Override
    public UserDTO create(UserCreationDTO userCreationDTO, MultipartFile fileAvatar, ERole eRole) {
        if (userRepository.existsByPhoneNumber(userCreationDTO.getPhoneNumber())) {
            throw new ConflictException(
                    Collections.singletonMap("phoneNumber", userCreationDTO.getPhoneNumber()));
        }

        User createUser = userMapper.toEntity(userCreationDTO);
        createUser.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));
        createUser.setAvatar(fileService.uploadFile(fileAvatar));

        Role role = roleRepository.findByName(eRole.name())
                .orElseThrow(() -> new InternalServerErrorException(eRole.name()));
        createUser.setRole(role);

        Vip defaultVip = vipRepository.findFirstByOrderByDepositMilestonesAsc()
                .orElseThrow(() -> new InternalServerErrorException("default vip"));
        createUser.setVip(defaultVip);
        
        createUser.setStatus(Default.User.STATUS.name());

        return userMapper.toDTO(userRepository.save(createUser));
    }

    @Override
    public UserProfileDTO update(long id, UserProfileDTO userProfileDTO, MultipartFile fileAvatar) {
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        if (userRepository.existsByIdNotAndPhoneNumber(id, userProfileDTO.getPhoneNumber())) {
            throw new ResourceNotFoundException(
                    Collections.singletonMap("phoneNumber", userProfileDTO.getPhoneNumber()));
        }

        User updateUser = userMapper.toEntity(userProfileDTO);
        updateUser.setId(oldUser.getId());
        updateUser.setPassword(oldUser.getPassword());
        // check update file Avatar
        if (!StringUtils.equals(updateUser.getAvatar(), oldUser.getAvatar())
                || (fileAvatar != null && !fileAvatar.isEmpty())) {
            fileService.deleteFile(oldUser.getAvatar());
            updateUser.setAvatar(fileService.uploadFile(fileAvatar));
        }
        updateUser.setRole(oldUser.getRole());
        updateUser.setVip(oldUser.getVip());
        updateUser.setStatus(oldUser.getStatus());

        return userMapper.toProfileDTO(userRepository.save(updateUser));
    }

    @Override
    public boolean lockById(long id) {
        return updateStatusById(id, EStatus.LOCK);
    }

    @Override
    public boolean unlockById(long id) {
        return updateStatusById(id, EStatus.ACTIVE);
    }

    public boolean updateStatusById(long id, EStatus eStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        user.setStatus(eStatus.name());
        userRepository.save(user);

        return true;
    }

}
