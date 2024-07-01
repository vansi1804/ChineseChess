package com.nvs.data.mapper;

import com.nvs.data.dto.VipDTO;
import com.nvs.data.entity.Vip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VipMapper{

   Vip toEntity(VipDTO vipDTO);

   VipDTO toDTO(Vip vip);

}
