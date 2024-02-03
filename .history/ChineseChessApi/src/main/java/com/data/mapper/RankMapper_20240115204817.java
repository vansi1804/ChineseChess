package com.data.mapper;

import com.data.dto.RankDTO;
import com.data.entity.Rank;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RankMapper {
  Rank toEntity(RankDTO rankDTO);

  RankDTO toDTO(Rank rank);
}
