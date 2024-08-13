package com.nvs.data.repository;

import com.nvs.data.entity.Training;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

  @Query("SELECT t" +
      " FROM Training t" +
      " WHERE t.parentTraining IS NULL")
  List<Training> findAllBase();

  @Query("SELECT CASE" +
      " WHEN COUNT(t.id) > 0 THEN TRUE ELSE FALSE END" +
      " FROM Training t" +
      " WHERE ((:parentTrainingId IS NULL AND t.parentTraining IS NULL)" +
      "        OR (t.parentTraining IS NOT NULL AND t.parentTraining.id = :parentTrainingId))" +
      "    AND t.title = :title")
  boolean existByParentTrainingIdAndTitle(@Param("parentTrainingId") Long parentTrainingId,
      @Param("title") String title);

}
