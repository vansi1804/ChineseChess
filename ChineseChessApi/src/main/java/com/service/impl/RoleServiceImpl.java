package com.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.RoleDTO;
import com.config.exception.ResourceNotFoundException;
import com.data.mapper.RoleMapper;
import com.data.repository.RoleRepository;
import com.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(r -> roleMapper.toDTO(r))
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO findById(int id) {
        return roleRepository.findById(id)
                .map(r -> roleMapper.toDTO(r))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("id", id)));
    }

    @Override
    public RoleDTO findByName(String name) {
        return roleRepository.findByName(name)
                .map(r -> roleMapper.toDTO(r))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("name", name)));
    }

}
