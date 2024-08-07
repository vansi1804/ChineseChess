package com.nvs.data.mapper;

import com.nvs.data.dto.RoleDTO;
import com.nvs.data.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper{

   Role toEntity(RoleDTO roleDTO);

   RoleDTO toDTO(Role role);

}
