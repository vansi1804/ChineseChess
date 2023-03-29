package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.RoleDTO;
import com.data.entity.Role;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RoleMapper {
    @Mapping(ignore = true, target = "users")
    Role toEntity(RoleDTO roleDTO);

    @Mapping(source = "users", target = "userDTOs")
    RoleDTO toDTO(Role role);
}
