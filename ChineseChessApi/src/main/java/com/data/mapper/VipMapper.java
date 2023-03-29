package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.VipDTO;
import com.data.entity.Vip;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface VipMapper {
    @Mapping(ignore = true, target = "users")
    Vip toEntity(VipDTO vipDTO);
    
    @Mapping(source = "users", target = "userDTOs")
    VipDTO toDTO(Vip vip);
}
