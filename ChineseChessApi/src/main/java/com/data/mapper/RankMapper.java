package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.RankDTO;
import com.data.entity.Rank;

@Mapper(componentModel = "spring")
public interface RankMapper {
    
    Rank toEntity(RankDTO rankDTO);

    RankDTO toDTO(Rank rank);
}
