package com.service;

import com.data.dto.RankDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface RankService {
  List<RankDTO> findAll();

  RankDTO findById(int id);

  RankDTO findByName(String name);

  @Transactional
  RankDTO create(RankDTO rankDTO);

  @Transactional
  RankDTO update(int id, RankDTO rankDTO);

  @Transactional
  boolean delete(int id);
}
