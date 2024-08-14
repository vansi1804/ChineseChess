package com.nvs.service.impl;

import com.nvs.common.Default;
import com.nvs.common.enumeration.ERole;
import com.nvs.common.enumeration.EStatus;
import com.nvs.config.exception.ConflictExceptionCustomize;
import com.nvs.config.exception.InternalServerErrorExceptionCustomize;
import com.nvs.config.exception.InvalidExceptionCustomize;
import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
import com.nvs.config.i18nMessage.Translator;
import com.nvs.data.dto.user.UserChangePasswordRequestDTO;
import com.nvs.data.dto.user.UserCreationDTO;
import com.nvs.data.dto.user.UserDTO;
import com.nvs.data.dto.user.UserProfileDTO;
import com.nvs.data.entity.Role;
import com.nvs.data.entity.User;
import com.nvs.data.entity.Vip;
import com.nvs.data.mapper.UserMapper;
import com.nvs.data.repository.RoleRepository;
import com.nvs.data.repository.UserRepository;
import com.nvs.data.repository.VipRepository;
import com.nvs.service.FileService;
import com.nvs.service.UserService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final RoleRepository roleRepository;
  private final VipRepository vipRepository;
  private final FileService fileService;
  private final PasswordEncoder passwordEncoder;
  private final AuditorAware<Long> auditorAware;

  @Override
  @Cacheable(value = "users", key = "#no + '-' + #limit + '-' + #sortBy")
  public Page<UserDTO> findAll(int no, int limit, String sortBy) {
    log.debug("Fetching all users with pagination, page number: {}, limit: {}, sorted by: {}", no,
        limit, sortBy);
    Page<UserDTO> users = userRepository.findAll(PageRequest.of(no, limit, Sort.by(sortBy)))
        .map(userMapper::toDTO);
    log.debug("Found {} users", users.getTotalElements());
    return users;
  }

  @Override
  @Cacheable(value = "users", key = "#id")
  public UserDTO findById(long id) {
    log.debug("Fetching user by ID: {}", id);
    UserDTO userDTO = userRepository.findById(id)
        .map(userMapper::toDTO)
        .orElseThrow(
            () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));
    log.debug("Found user: {}", userDTO);
    return userDTO;
  }

  @Override
  @Cacheable(value = "users", key = "#phoneNumber")
  public UserDTO findByPhoneNumber(String phoneNumber) {
    log.debug("Fetching user by phone number: {}", phoneNumber);
    UserDTO userDTO = userRepository.findByPhoneNumber(phoneNumber)
        .map(userMapper::toDTO)
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("phoneNumber", phoneNumber)));
    log.debug("Found user: {}", userDTO);
    return userDTO;
  }

  @Override
  @Cacheable(value = "users", key = "#name")
  public UserDTO findByName(String name) {
    log.debug("Fetching user by name: {}", name);
    UserDTO userDTO = userRepository.findByName(name)
        .map(userMapper::toDTO)
        .orElseThrow(
            () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("name", name)));
    log.debug("Found user: {}", userDTO);
    return userDTO;
  }

  @Override
  @CachePut(value = "users", key = "#userCreationDTO.phoneNumber")
  public UserDTO create(UserCreationDTO userCreationDTO, ERole eRole) {
    log.debug("Creating user with DTO: {} and role: {}", userCreationDTO, eRole);

    if (userRepository.existsByPhoneNumber(userCreationDTO.getPhoneNumber())) {
      throw new ConflictExceptionCustomize(
          Collections.singletonMap("phoneNumber", userCreationDTO.getPhoneNumber()));
    }

    User createUser = userMapper.toEntity(userCreationDTO);
    createUser.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));

    Role role = roleRepository.findByName(eRole.name())
        .orElseThrow(() -> new InternalServerErrorExceptionCustomize(eRole.name()));
    createUser.setRole(role);

    Vip defaultVip = vipRepository.findFirstByOrderByDepositMilestonesAsc()
        .orElseThrow(() -> new InternalServerErrorExceptionCustomize("default vip"));
    createUser.setVip(defaultVip);

    createUser.setStatus(Default.User.STATUS.name());

    UserDTO createdUserDTO = userMapper.toDTO(userRepository.save(createUser));
    log.debug("Created user: {}", createdUserDTO);
    return createdUserDTO;
  }

  @Override
  @CachePut(value = "users", key = "#id")
  public UserProfileDTO update(long id, UserProfileDTO userProfileDTO) {
    log.debug("Updating user with ID: {} and DTO: {}", id, userProfileDTO);

    User existingUser = userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));

    if (userRepository.existsByIdNotAndPhoneNumber(id, userProfileDTO.getPhoneNumber())) {
      throw new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("phoneNumber", userProfileDTO.getPhoneNumber()));
    }

    User updateUser = userMapper.toEntity(userProfileDTO);
    updateUser.setId(existingUser.getId());
    updateUser.setPassword(existingUser.getPassword());
    // Check and update file Avatar
    if (!StringUtils.equals(updateUser.getAvatar(), existingUser.getAvatar())) {
      log.debug("Avatar updated. Deleting old avatar: {}", existingUser.getAvatar());
      fileService.deleteFile(existingUser.getAvatar());
    }
    updateUser.setRole(existingUser.getRole());
    updateUser.setVip(existingUser.getVip());
    updateUser.setStatus(existingUser.getStatus());
    updateUser.setCreatedByUserId(existingUser.getCreatedByUserId());
    updateUser.setCreatedDate(existingUser.getCreatedDate());

    User updatedUser = userRepository.save(updateUser);
    userRepository.flush();

    UserProfileDTO updatedUserProfileDTO = userMapper.toProfileDTO(updatedUser);
    log.debug("Updated user: {}", updatedUserProfileDTO);
    return updatedUserProfileDTO;
  }

  @Override
  @CachePut(value = "users", key = "#id")
  public UserDTO lockById(long id) {
    log.debug("Locking user with ID: {}", id);
    UserDTO lockedUserDTO = this.updateStatusById(id, EStatus.LOCK);
    log.debug("Locked user: {}", lockedUserDTO);
    return lockedUserDTO;
  }

  @Override
  @CachePut(value = "users", key = "#id")
  public UserDTO unlockById(long id) {
    log.debug("Unlocking user with ID: {}", id);
    UserDTO unlockedUserDTO = this.updateStatusById(id, EStatus.ACTIVE);
    log.debug("Unlocked user: {}", unlockedUserDTO);
    return unlockedUserDTO;
  }

  @Override
  public boolean isCurrentUser(long id) {
    boolean isCurrent = Objects.equals(id, auditorAware.getCurrentAuditor().orElse(null));
    log.debug("Checking if ID {} is the current user: {}", id, isCurrent);
    return !isCurrent;
  }

  @Override
  public UserProfileDTO changePassword(long id,
      UserChangePasswordRequestDTO userChangePasswordRequestDTO) {
    log.debug("Changing password for user ID: {}", id);

    if (this.isCurrentUser(id)) {
      throw new AccessDeniedException(null);
    }

    User user = userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));

    if (!passwordEncoder.matches(userChangePasswordRequestDTO.getOldPassword(),
        user.getPassword())) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("id", id);
      errors.put("message", Translator.toLocale("ERROR_OLD_PASSWORD"));

      throw new InvalidExceptionCustomize(errors);
    }

    if (!userChangePasswordRequestDTO.getNewPassword()
        .equals(userChangePasswordRequestDTO.getNewPasswordConfirm())) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("id", id);
      errors.put("message", Translator.toLocale("ERROR_NEW_PASSWORD_CONFIRM"));

      throw new InvalidExceptionCustomize(errors);
    }

    user.setPassword(passwordEncoder.encode(userChangePasswordRequestDTO.getNewPasswordConfirm()));

    UserProfileDTO updatedProfileDTO = userMapper.toProfileDTO(userRepository.save(user));
    log.debug("Password changed successfully for user ID: {}", id);
    return updatedProfileDTO;
  }

  @CachePut(value = "users", key = "#id")
  public UserDTO updateStatusById(long id, EStatus eStatus) {
    log.debug("Updating status for user ID: {} to {}", id, eStatus);

    User user = userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));

    user.setStatus(eStatus.name());

    UserDTO updatedUserDTO = userMapper.toDTO(userRepository.save(user));
    log.debug("Updated user status: {}", updatedUserDTO);
    return updatedUserDTO;
  }
}
