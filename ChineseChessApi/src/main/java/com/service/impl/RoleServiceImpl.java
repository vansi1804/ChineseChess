package com.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.ErrorMessage;
import com.dto.RoleDTO;
import com.entity.Role;
import com.exception.ExceptionCustom;
import com.mapper.RoleMapper;
import com.repository.RoleRepository;
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
        return roleMapper.toDTO(roleRepository.findById(id)
                .orElseThrow(() -> new ExceptionCustom("Role", ErrorMessage.DATA_NOT_FOUND, "id", id)));
    }

    @Override
    public RoleDTO create(RoleDTO roleDTO) {
        if (roleRepository.findByName(roleDTO.getName()) != null)
            throw new ExceptionCustom("Role", ErrorMessage.EXISTING_NAME, "name", roleDTO.getName());
        return roleMapper.toDTO(roleRepository.save(roleMapper.toEntity(roleDTO)));
    }

    @Override
    public RoleDTO update(int id, RoleDTO roleDTO) {
        Role role = roleRepository.findByName(roleDTO.getName());
        if ((role != null) && (role.getId() != id))
            throw new ExceptionCustom("Role", ErrorMessage.EXISTING_NAME, "name", roleDTO.getName());
        roleRepository.save(roleMapper.toEntity(roleDTO));
        return roleDTO;
    }

    @Override
    public void delete(int id) {
        roleRepository.deleteById(id);
    }

}
