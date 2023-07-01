package com.data.mapper;

import org.mapstruct.Mapper;

import com.data.dto.RoleDTO;
import com.data.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    
    Role toEntity(RoleDTO roleDTO);

    RoleDTO toDTO(Role role);
}
