package com.nvs.data.mapper;

import com.nvs.data.dto.RankDTO;
import com.nvs.data.entity.Rank;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RankMapper {
    Rank toEntity(RankDTO rankDTO);

    RankDTO toDTO(Rank rank);
}
