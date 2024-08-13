package com.nvs.data.mapper;

import com.nvs.data.dto.user.UserCreationDTO;
import com.nvs.data.dto.user.UserDTO;
import com.nvs.data.dto.user.UserProfileDTO;
import com.nvs.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, VipMapper.class})
public interface UserMapper {

  User toEntity(UserCreationDTO userCreationDTO);

  User toEntity(UserProfileDTO userProfileDTO);

  @Mapping(source = "vip", target = "vipDTO")
  UserProfileDTO toProfileDTO(User user);

  @Mapping(expression = "java(toProfileDTO(user))", target = "userProfileDTO")
  @Mapping(source = "role", target = "roleDTO")
  UserDTO toDTO(User user);

}
