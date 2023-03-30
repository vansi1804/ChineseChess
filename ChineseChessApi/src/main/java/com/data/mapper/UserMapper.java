package com.data.mapper;

import java.security.NoSuchAlgorithmException;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.UserDTO;
import com.data.dto.creation.UserCreationDTO;
import com.data.dto.profile.UserProfileDTO;
import com.data.entity.User;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, VipMapper.class})
public interface UserMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "createdAt")
    @Mapping(ignore = true, target = "password")
    @Mapping(ignore = true, target = "role")
    @Mapping(ignore = true, target = "vip")
    @Mapping(ignore = true, target = "status")
    User toEntity(UserCreationDTO userCreationDTO) throws NoSuchAlgorithmException;

    @Mapping(source = "userProfileDTO.phoneNumber", target = "phoneNumber")
    @Mapping(ignore = true, target = "password")
    @Mapping(source = "userProfileDTO.name", target = "name")
    @Mapping(source = "userProfileDTO.avatar", target = "avatar")
    @Mapping(ignore = true, target = "vip")
    @Mapping(ignore = true, target = "role")
    User toEntity(UserDTO userDTO);

    @Mapping(source = "vip.name", target = "vipName")
    UserProfileDTO toProfileDTO(User user);

    @Mapping(source = "phoneNumber", target = "userProfileDTO.phoneNumber")
    @Mapping(source = "name", target = "userProfileDTO.name")
    @Mapping(source = "avatar", target = "userProfileDTO.avatar")
    @Mapping(source = "vip.name", target = "userProfileDTO.vipName")
    @Mapping(source = "role.name", target = "roleName")
    UserDTO toDTO(User user);
}
