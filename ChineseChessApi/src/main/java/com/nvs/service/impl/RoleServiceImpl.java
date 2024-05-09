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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Override
  public List<RoleDTO> findAll() {
    return roleRepository
        .findAll()
        .stream()
        .map(r -> roleMapper.toDTO(r))
        .collect(Collectors.toList());
  }

  @Override
  public RoleDTO findById(int id) {
    return roleRepository
        .findById(id)
        .map(r -> roleMapper.toDTO(r))
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("id", id)));
  }

  @Override
  public RoleDTO findByName(String name) {
    return roleRepository
        .findByName(name)
        .map(r -> roleMapper.toDTO(r))
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("name", name)));
  }
}
