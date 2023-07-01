package com.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.data.entity.Training;

public interface TrainingRepository extends JpaRepository<Training, Long> {

        @Query("SELECT t"
                        + " FROM Training t"
                        + " WHERE ((:parentTrainingId IS NULL AND t.parentTraining IS NULL)"
                        + "        OR (t.parentTraining IS NOT NULL AND t.parentTraining.id = :parentTrainingId))")
        List<Training> findAllByParentTraining_Id(@Param("parentTrainingId") Long parentTrainingId);

        @Query("SELECT CASE"
                        + " WHEN COUNT(t.id) > 0 THEN TRUE"
                        + " ELSE FALSE END"
                        + " FROM Training t"
                        + " WHERE ((:parentTrainingId IS NULL AND t.parentTraining IS NULL)"
                        + "        OR (t.parentTraining IS NOT NULL AND t.parentTraining.id = :parentTrainingId))"
                        + "    AND t.title = :title")
        boolean existByParentTrainingIdAndTitle(
                        @Param("parentTrainingId") Long parentTrainingId, @Param("title") String title);

}
