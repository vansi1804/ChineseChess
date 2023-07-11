package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.RankDTO;
import com.data.entity.Rank;

@Mapper(componentModel = "spring")
public interface RankMapper {
    
    @Mapping(ignore = true, target = "createdDate")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(ignore = true, target = "lastModifiedDate")
    @Mapping(ignore = true, target = "lastModifiedBy")
    Rank toEntity(RankDTO rankDTO);

    @Mapping(source = "createdBy.id", target = "createdByUserId")
    @Mapping(source = "lastModifiedBy.id", target = "lastModifiedByUserId")
    RankDTO toDTO(Rank rank);
}
