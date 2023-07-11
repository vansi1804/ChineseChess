package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.VipDTO;
import com.data.entity.Vip;

@Mapper(componentModel = "spring")
public interface VipMapper {
    
    @Mapping(ignore = true, target = "createdDate")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(ignore = true, target = "lastModifiedDate")
    @Mapping(ignore = true, target = "lastModifiedBy")
    Vip toEntity(VipDTO vipDTO);
    
    @Mapping(source = "createdBy.id", target = "createdByUserId")
    @Mapping(source = "lastModifiedBy.id", target = "lastModifiedByUserId")
    VipDTO toDTO(Vip vip);
}
