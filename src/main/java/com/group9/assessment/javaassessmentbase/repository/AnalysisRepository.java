package com.group9.assessment.javaassessmentbase.repository;

import com.group9.assessment.javaassessmentbase.repository.entities.AnalysisReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisRepository extends JpaRepository<AnalysisReportEntity, Long> {}
