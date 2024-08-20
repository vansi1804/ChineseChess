package com.nvs.service.impl;

import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
import com.nvs.data.dto.RoleDTO;
import com.nvs.data.mapper.RoleMapper;
import com.nvs.data.repository.RoleRepository;
import com.nvs.service.RoleService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Cacheable(value = "roles")
  @Override
  public List<RoleDTO> findAll() {
    log.debug("-- Finding all roles");
    List<RoleDTO> roles = roleRepository.findAll().stream().map(roleMapper::toDTO)
        .collect(Collectors.toList());
    log.debug("-- Found {} roles", roles.size());
    return roles;
  }

  @Cacheable(value = "roleById", key = "#id")
  @Override
  public RoleDTO findById(int id) {
    log.debug("-- Finding role by ID: {}", id);
    RoleDTO roleDTO = roleRepository.findById(id).map(roleMapper::toDTO).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));
    log.debug("-- Found role: {}", roleDTO);
    return roleDTO;
  }

  @Cacheable(value = "roleByName", key = "#name")
  @Override
  public RoleDTO findByName(String name) {
    log.debug("-- Finding role by name: {}", name);
    RoleDTO roleDTO = roleRepository.findByName(name).map(roleMapper::toDTO).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("name", name)));
    log.debug("-- Found role: {}", roleDTO);
    return roleDTO;
  }

}
