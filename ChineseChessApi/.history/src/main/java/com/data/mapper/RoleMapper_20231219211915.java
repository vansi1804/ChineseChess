package com.data.mapper;

import com.data.dto.RoleDTO;
import com.data.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  Role toEntity(RoleDTO roleDTO);

  RoleDTO toDTO(Role role);
}
