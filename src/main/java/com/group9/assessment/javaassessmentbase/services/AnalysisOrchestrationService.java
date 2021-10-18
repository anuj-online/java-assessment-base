package com.group9.assessment.javaassessmentbase.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.group9.assessment.javaassessmentbase.JavaAssessmentBaseApplication;
import com.group9.assessment.javaassessmentbase.repository.MasterRecordRepository;
import com.group9.assessment.javaassessmentbase.repository.RowRepository;
import com.group9.assessment.javaassessmentbase.repository.entities.AnalysisReport;
import com.group9.assessment.javaassessmentbase.repository.entities.AnalysisReportEntity;
import com.group9.assessment.javaassessmentbase.repository.entities.MasterRecordEntity;
import com.group9.assessment.javaassessmentbase.repository.entities.State;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Slf4j
public class AnalysisOrchestrationService {

  public static final int MAX_WIDTH = 255;
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new ObjectMapper();
    OBJECT_MAPPER.registerModule(new ParameterNamesModule());
    OBJECT_MAPPER.registerModule(new Jdk8Module());
    OBJECT_MAPPER.registerModule(new JavaTimeModule());
  }

  private final MasterRecordRepository masterRecordRepository;
  private final FileService fileService;
  private final RowRepository rowRepository;

  public AnalysisOrchestrationService(
      MasterRecordRepository masterRecordRepository,
      FileService fileService,
      RowRepository rowRepository) {
    this.masterRecordRepository = masterRecordRepository;
    this.fileService = fileService;
    this.rowRepository = rowRepository;
  }

  @Async
  public void startAnalysis(long masterRecordId) {
    var begin = Instant.now();
    JavaAssessmentBaseApplication.logMemory();
    Optional<MasterRecordEntity> analysis = masterRecordRepository.findById(masterRecordId);
    analysis.ifPresentOrElse(
        a -> {
          var masterRecordEntity = masterRecordRepository.save(a.setState(State.ANALYSING));

          try {

            fileService.readFileAndSaveRows(a.getFileUrl(), a);

            var analysisData = rowRepository.getAnalysisData(a);
            log.info("analysis is {}", analysisData);
            var analysisReportEntity = getAnalysisReportEntity(analysisData);

            masterRecordEntity =
                createMasterRecordEntity(begin, masterRecordEntity, Optional.empty());
            masterRecordEntity.setAnalysisReportEntity(analysisReportEntity);
            masterRecordRepository.save(masterRecordEntity);

          } catch (Exception e) {
            log.error("error processing", e);
            masterRecordRepository.save(
                createMasterRecordEntity(
                    begin,
                    masterRecordEntity,
                    Optional.of(StringUtils.abbreviate(e.getMessage(), MAX_WIDTH))));
          }
        },
        () -> log.error("Invalid analysisId {}, entity not found", masterRecordId));
    JavaAssessmentBaseApplication.logMemory();
  }

  private AnalysisReportEntity getAnalysisReportEntity(AnalysisReport analysisData) {
    return OBJECT_MAPPER.convertValue(analysisData, AnalysisReportEntity.class);
  }

  private MasterRecordEntity createMasterRecordEntity(
      Instant begin, MasterRecordEntity masterRecordEntity, Optional<String> failedSummary) {
    if (failedSummary.isPresent()) {
      return masterRecordEntity
          .setState(State.FAILED)
          .setFailedSummary(failedSummary.get())
          .setAnalyseDate(LocalDateTime.now())
          .setAnalyseTimeInSeconds(ChronoUnit.SECONDS.between(begin, Instant.now()));
    } else {
      return masterRecordEntity
          .setState(State.FINISHED)
          .setAnalyseDate(LocalDateTime.now())
          .setAnalyseTimeInSeconds(ChronoUnit.SECONDS.between(begin, Instant.now()));
    }
  }
}
