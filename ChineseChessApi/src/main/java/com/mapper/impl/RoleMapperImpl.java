package com.mapper.impl;

import org.springframework.stereotype.Component;

import com.dto.RoleDTO;
import com.entity.Role;
import com.mapper.RoleMapper;

@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null)
            return null;
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        role.setPower(roleDTO.getPower());
        return role;
    }

    @Override
    public RoleDTO toDTO(Role role) {
        if (role == null)
            return null;
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setPower(role.getPower());
        return roleDTO;
    }

}
