package com.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.ErrorMessage;
import com.data.dto.RoleDTO;
import com.data.entity.Role;
import com.exception.ExceptionCustom;
import com.data.mapper.RoleMapper;
import com.data.repository.RoleRepository;
import com.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream().map(r -> roleMapper.toDTO(r)).collect(Collectors.toList());
    }

    @Override
    public RoleDTO findById(int id) {
        return roleRepository.findById(id).map(r -> roleMapper.toDTO(r))
                .orElseThrow(() -> new ExceptionCustom("Role", ErrorMessage.DATA_NOT_FOUND, "id", id));
    }

    @Override
    public RoleDTO findByName(String name) {
        return roleRepository.findByName(name).map(r -> roleMapper.toDTO(r))
                .orElseThrow(() -> new ExceptionCustom("Role", ErrorMessage.DATA_NOT_FOUND, "name", name));
    }

    @Override
    public RoleDTO create(RoleDTO roleDTO) {
        if (roleRepository.findByName(roleDTO.getName()).isPresent()) {
            throw new ExceptionCustom("Role", ErrorMessage.DATA_EXISTING, "name", roleDTO.getName());
        }
        return roleMapper.toDTO(roleRepository.save(roleMapper.toEntity(roleDTO)));
    }

    @Override
    public RoleDTO update(int id, RoleDTO roleDTO) {
        Optional<Role> role = roleRepository.findByName(roleDTO.getName());
        if (role.isPresent() && (role.get().getId() != id)) {
            throw new ExceptionCustom("Role", ErrorMessage.DATA_EXISTING, "name", roleDTO.getName());
        }
        return roleMapper.toDTO(roleRepository.save(roleMapper.toEntity(roleDTO)));
    }

    @Override
    public void delete(int id) {
        roleRepository.delete(roleRepository.findById(id)
                .orElseThrow(() -> new ExceptionCustom("Role", ErrorMessage.DATA_NOT_FOUND, "id", id)));
    }

}
