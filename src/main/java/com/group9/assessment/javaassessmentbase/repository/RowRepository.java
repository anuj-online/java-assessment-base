package com.group9.assessment.javaassessmentbase.repository;

import com.group9.assessment.javaassessmentbase.repository.entities.AnalysisReport;
import com.group9.assessment.javaassessmentbase.repository.entities.MasterRecordEntity;
import com.group9.assessment.javaassessmentbase.repository.entities.RowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RowRepository extends JpaRepository<RowEntity, Long> {

  @Query(
      "select new com.group9.assessment.javaassessmentbase.repository.entities.AnalysisReport("
          + " MAX(r.creationDate), MIN(r.creationDate), COUNT(1), SUM(r.acceptedAnswerId)"
          + ", AVG(r.score))"
          + " from RowEntity as r where masterRecordEntity = ?1")
  AnalysisReport getAnalysisData(MasterRecordEntity masterRecordEntity);
}
