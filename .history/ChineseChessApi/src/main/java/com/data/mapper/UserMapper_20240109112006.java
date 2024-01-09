package com.data.mapper;

import com.data.dto.user.UserCreationDTO;
import com.data.dto.user.UserDTO;
import com.data.dto.user.UserProfileDTO;
import com.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { RoleMapper.class, VipMapper.class })
public interface UserMapper {
  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "password")
  @Mapping(ignore = true, target = "vip")
  @Mapping(ignore = true, target = "role")
  @Mapping(ignore = true, target = "status")
  @Mapping(ignore = true, target = "createdDate")
  @Mapping(ignore = true, target = "createdByUserId")
  @Mapping(ignore = true, target = "lastModifiedDate")
  @Mapping(ignore = true, target = "lastModifiedByUserId")
  User toEntity(UserCreationDTO userCreationDTO);

  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "password")
  @Mapping(ignore = true, target = "vip")
  @Mapping(ignore = true, target = "role")
  @Mapping(ignore = true, target = "status")
  @Mapping(ignore = true, target = "createdDate")
  @Mapping(ignore = true, target = "createdByUserId")
  @Mapping(ignore = true, target = "lastModifiedDate")
  @Mapping(ignore = true, target = "lastModifiedByUserId")
  User toEntity(UserProfileDTO userProfileDTO);

  @Mapping(source = "vip", target = "vipDTO")
  UserProfileDTO toProfileDTO(User user);

  @Mapping(expression = "java(toProfileDTO(user))", target = "userProfileDTO")
  @Mapping(source = "role", target = "roleDTO")
  UserDTO toDTO(User user);
}
