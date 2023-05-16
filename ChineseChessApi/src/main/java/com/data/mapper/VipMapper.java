package com.data.mapper;

import org.mapstruct.Mapper;

import com.data.dto.VipDTO;
import com.data.entity.Vip;

@Mapper(componentModel = "spring")
public interface VipMapper {
    Vip toEntity(VipDTO vipDTO);
    
    VipDTO toDTO(Vip vip);
}
