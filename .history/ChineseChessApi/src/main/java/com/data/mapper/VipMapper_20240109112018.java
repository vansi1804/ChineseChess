package com.data.mapper;

import com.data.dto.VipDTO;
import com.data.entity.Vip;
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
