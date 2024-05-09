package com.nvs.service.impl;

import com.nvs.common.Default;
import com.nvs.common.ErrorMessage;
import com.nvs.common.enumeration.ERole;
import com.nvs.common.enumeration.EStatus;
import com.nvs.config.exception.ConflictExceptionCustomize;
import com.nvs.config.exception.InternalServerErrorExceptionCustomize;
import com.nvs.config.exception.InvalidExceptionCustomize;
import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final RoleRepository roleRepository;
  private final VipRepository vipRepository;
  private final FileService fileService;
  private final PasswordEncoder passwordEncoder;
  private final AuditorAware<Long> auditorAware;

  @Override
  public Page<UserDTO> findAll(int no, int limit, String sortBy) {
    return userRepository
        .findAll(PageRequest.of(no, limit, Sort.by(sortBy)))
        .map(u -> userMapper.toDTO(u));
  }

  @Override
  public UserDTO findById(long id) {
    return userRepository
        .findById(id)
        .map(u -> userMapper.toDTO(u))
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("id", id)));
  }

  @Override
  public UserDTO findByPhoneNumber(String phoneNumber) {
    return userRepository
        .findByPhoneNumber(phoneNumber)
        .map(u -> userMapper.toDTO(u))
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("phoneNumber", phoneNumber)));
  }

  @Override
  public UserDTO findByName(String name) {
    return userRepository
        .findByName(name)
        .map(u -> userMapper.toDTO(u))
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("name", name)));
  }

  @Override
  public UserDTO create(UserCreationDTO userCreationDTO, ERole eRole) {
    if (userRepository.existsByPhoneNumber(userCreationDTO.getPhoneNumber())) {
      throw new ConflictExceptionCustomize(
          Collections.singletonMap(
              "phoneNumber",
              userCreationDTO.getPhoneNumber()));
    }

    User createUser = userMapper.toEntity(userCreationDTO);
    createUser.setPassword(
        passwordEncoder.encode(userCreationDTO.getPassword()));

    Role role = roleRepository
        .findByName(eRole.name())
        .orElseThrow(() -> new InternalServerErrorExceptionCustomize(eRole.name()));
    createUser.setRole(role);

    Vip defaultVip = vipRepository
        .findFirstByOrderByDepositMilestonesAsc()
        .orElseThrow(() -> new InternalServerErrorExceptionCustomize("default vip"));
    createUser.setVip(defaultVip);

    createUser.setStatus(Default.User.STATUS.name());

    return userMapper.toDTO(userRepository.save(createUser));
  }

  @Override
  public UserProfileDTO update(long id, UserProfileDTO userProfileDTO) {
    User existingUser = userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("id", id)));

    if (userRepository.existsByIdNotAndPhoneNumber(
        id,
        userProfileDTO.getPhoneNumber())) {
      throw new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("phoneNumber", userProfileDTO.getPhoneNumber()));
    }

    User updateUser = userMapper.toEntity(userProfileDTO);
    updateUser.setId(existingUser.getId());
    updateUser.setPassword(existingUser.getPassword());
    // check update file Avatar
    if (!StringUtils.equals(updateUser.getAvatar(), existingUser.getAvatar())) {
      fileService.deleteFile(existingUser.getAvatar());
    }
    updateUser.setRole(existingUser.getRole());
    updateUser.setVip(existingUser.getVip());
    updateUser.setStatus(existingUser.getStatus());
    updateUser.setCreatedByUserId(existingUser.getCreatedByUserId());
    updateUser.setCreatedDate(existingUser.getCreatedDate());

    User updatedUser = userRepository.save(updateUser);
    userRepository.flush();

    return userMapper.toProfileDTO(updatedUser);
  }

  @Override
  public UserDTO lockById(long id) {
    return this.updateStatusById(id, EStatus.LOCK);
  }

  @Override
  public UserDTO unlockById(long id) {
    return this.updateStatusById(id, EStatus.ACTIVE);
  }

  public UserDTO updateStatusById(long id, EStatus eStatus) {
    User user = userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("id", id)));

    user.setStatus(eStatus.name());

    return userMapper.toDTO(userRepository.save(user));
  }

  @Override
  public boolean isCurrentUser(long id) {
    Long currentAuthId = auditorAware.getCurrentAuditor().orElse(null);
    return Objects.equals(id, currentAuthId);
  }

  @Override
  public UserProfileDTO changePassword(
      long id,
      UserChangePasswordRequestDTO userChangePasswordRequestDTO) {
    if (!this.isCurrentUser(id)) {
      throw new AccessDeniedException(null);
    }

    User user = userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("id", id)));

    if (!passwordEncoder.matches(
        userChangePasswordRequestDTO.getOldPassword(),
        user.getPassword())) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("id", id);
      errors.put("message", ErrorMessage.ERROR_OLD_PASSWORD);

      throw new InvalidExceptionCustomize(errors);
    }

    if (!userChangePasswordRequestDTO
        .getNewPassword()
        .equals(userChangePasswordRequestDTO.getNewPasswordConfirm())) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("id", id);
      errors.put("message", ErrorMessage.ERROR_NEW_PASSWORD_CONFIRM);

      throw new InvalidExceptionCustomize(errors);
    }

    user.setPassword(
        passwordEncoder.encode(
            userChangePasswordRequestDTO.getNewPasswordConfirm()));

    return userMapper.toProfileDTO(userRepository.save(user));
  }
}
