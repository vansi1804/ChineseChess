package com.mapper;

import org.mapstruct.Mapper;

import com.dto.RoleDTO;
import com.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleDTO roleDTO);

    RoleDTO toDTO(Role role);
}
