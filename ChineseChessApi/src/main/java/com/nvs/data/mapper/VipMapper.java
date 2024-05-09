package com.nvs.data.mapper;

import com.nvs.data.entity.Vip;
import com.nvs.data.dto.VipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VipMapper {
    @Mapping(ignore = true, target = "createdDate")
    @Mapping(ignore = true, target = "createdByUserId")
    @Mapping(ignore = true, target = "lastModifiedDate")
    @Mapping(ignore = true, target = "lastModifiedByUserId")
    Vip toEntity(VipDTO vipDTO);

    VipDTO toDTO(Vip vip);
}
