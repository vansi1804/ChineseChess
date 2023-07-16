package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.user.UserCreationDTO;
import com.data.dto.user.UserDTO;
import com.data.dto.user.UserProfileDTO;
import com.data.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "password")
    @Mapping(ignore = true, target = "vip")
    @Mapping(ignore = true, target = "role")
    @Mapping(ignore = true, target = "status")
    @Mapping(ignore = true, target = "createdDate")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(ignore = true, target = "lastModifiedDate")
    @Mapping(ignore = true, target = "lastModifiedBy")
    User toEntity(UserCreationDTO userCreationDTO);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "password")
    @Mapping(ignore = true, target = "vip")
    @Mapping(ignore = true, target = "role")
    @Mapping(ignore = true, target = "status")
    @Mapping(ignore = true, target = "createdDate")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(ignore = true, target = "lastModifiedDate")
    @Mapping(ignore = true, target = "lastModifiedBy")
    User toEntity(UserProfileDTO userProfileDTO);

    @Mapping(source = "vip.name", target = "vipName")
    UserProfileDTO toProfileDTO(User user);

    @Mapping(source = "phoneNumber", target = "userProfileDTO.phoneNumber")
    @Mapping(source = "name", target = "userProfileDTO.name")
    @Mapping(source = "avatar", target = "userProfileDTO.avatar")
    @Mapping(source = "vip.name", target = "userProfileDTO.vipName")
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "createdBy.id", target = "createdByUserId")
    @Mapping(source = "lastModifiedBy.id", target = "lastModifiedByUserId")
    UserDTO toDTO(User user);

}
