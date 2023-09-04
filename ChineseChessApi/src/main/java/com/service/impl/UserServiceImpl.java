package com.service.impl;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.ERole;
import com.common.enumeration.EStatus;
import com.data.dto.user.UserCreationDTO;
import com.data.dto.user.UserDTO;
import com.data.dto.user.UserProfileDTO;
import com.data.entity.Role;
import com.data.entity.User;
import com.data.entity.Vip;
import com.config.exception.ConflictExceptionCustomize;
import com.config.exception.InternalServerErrorExceptionCustomize;
import com.config.exception.ResourceNotFoundExceptionCustomize;
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
    private final AuditorAware<Long> auditorAware;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            RoleRepository roleRepository,
            VipRepository vipRepository,
            FileService fileService,
            PasswordEncoder passwordEncoder,
            AuditorAware<Long> auditorAware) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.vipRepository = vipRepository;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
        this.auditorAware = auditorAware;
    }

    @Override
    public Page<UserDTO> findAll(int no, int limit, String sortBy) {
        return userRepository.findAll(PageRequest.of(no, limit, Sort.by(sortBy)))
                .map(u -> userMapper.toDTO(u));
    }

    @Override
    public UserDTO findById(long id) {
        return userRepository.findById(id)
                .map(u -> userMapper.toDTO(u))
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("id", id)));
    }

    @Override
    public UserDTO findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .map(u -> userMapper.toDTO(u))
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("phoneNumber", phoneNumber)));
    }

    @Override
    public UserDTO findByName(String name) {
        return userRepository.findByName(name)
                .map(u -> userMapper.toDTO(u))
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("name", name)));
    }

    @Override
    public UserDTO create(UserCreationDTO userCreationDTO, ERole eRole) {
        if (userRepository.existsByPhoneNumber(userCreationDTO.getPhoneNumber())) {
            throw new ConflictExceptionCustomize(
                    Collections.singletonMap("phoneNumber", userCreationDTO.getPhoneNumber()));
        }

        User createUser = userMapper.toEntity(userCreationDTO);
        createUser.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));

        Role role = roleRepository.findByName(eRole.name())
                .orElseThrow(
                        () -> new InternalServerErrorExceptionCustomize(eRole.name()));
        createUser.setRole(role);

        Vip defaultVip = vipRepository.findFirstByOrderByDepositMilestonesAsc()
                .orElseThrow(
                        () -> new InternalServerErrorExceptionCustomize("default vip"));
        createUser.setVip(defaultVip);

        createUser.setStatus(Default.User.STATUS.name());

        return userMapper.toDTO(userRepository.save(createUser));
    }

    @Override
    public UserProfileDTO update(long id, UserProfileDTO userProfileDTO) {
        User oldUser = userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("id", id)));

        if (userRepository.existsByIdNotAndPhoneNumber(id, userProfileDTO.getPhoneNumber())) {
            throw new ResourceNotFoundExceptionCustomize(
                    Collections.singletonMap("phoneNumber", userProfileDTO.getPhoneNumber()));
        }

        User updateUser = userMapper.toEntity(userProfileDTO);
        updateUser.setId(oldUser.getId());
        updateUser.setPassword(oldUser.getPassword());
        // check update file Avatar
        if (!StringUtils.equals(updateUser.getAvatar(), oldUser.getAvatar())) {
            fileService.deleteFile(oldUser.getAvatar());
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
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("id", id)));

        user.setStatus(eStatus.name());
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean isCurrentUser(long id) {
        Long userId = auditorAware.getCurrentAuditor().orElse(null);
        return (userId == null) || (userId == id);
    }

}
