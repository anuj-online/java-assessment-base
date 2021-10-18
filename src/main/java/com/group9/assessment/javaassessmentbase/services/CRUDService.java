package com.group9.assessment.javaassessmentbase.services;

import com.group9.assessment.javaassessmentbase.controllers.responses.AnalysisExtendedResponse;
import com.group9.assessment.javaassessmentbase.controllers.responses.AnalysisResponse;
import com.group9.assessment.javaassessmentbase.repository.MasterRecordRepository;
import com.group9.assessment.javaassessmentbase.repository.entities.MasterRecordEntity;
import com.group9.assessment.javaassessmentbase.repository.entities.State;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CRUDService {

  private final MasterRecordRepository masterRecordRepository;
  private final AnalysisOrchestrationService analysisOrchestrationService;

  public long saveAnalysis(URL url) {
    var id = masterRecordRepository.save(mapToAnalysis(url)).getId();
    analysisOrchestrationService.startAnalysis(id);
    return id;
  }

  public AnalysisResponse getAnalysis(Long id) {
    var masterRecordEntity = masterRecordRepository.findById(id);
    if (masterRecordEntity.isPresent()) {
      var masterRecord = masterRecordEntity.get();
      AnalysisResponse analysisResponse = getAnalysisResponse(masterRecord);
      if (masterRecord.getAnalysisReportEntity() != null) {
        addDetailsToAnalysisResponse(masterRecord, analysisResponse);
      }
      return analysisResponse;

    } else {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, String.format("Id %s is not valid", id));
    }
  }

  private void addDetailsToAnalysisResponse(
      MasterRecordEntity masterRecord, AnalysisResponse analysisResponse) {
    analysisResponse.setDetailsResponse(
        new AnalysisResponse.DetailsResponse()
            .setAvgScore(masterRecord.getAnalysisReportEntity().getAvgScore())
            .setFirstPost(masterRecord.getAnalysisReportEntity().getFirstPost())
            .setLastPost(masterRecord.getAnalysisReportEntity().getLastPost())
            .setTotalAcceptedPosts(masterRecord.getAnalysisReportEntity().getTotalAcceptedPosts())
            .setTotalPosts(masterRecord.getAnalysisReportEntity().getTotalPosts()));
  }

  private AnalysisResponse getAnalysisResponse(MasterRecordEntity masterRecord) {
    return new AnalysisResponse()
        .setId(masterRecord.getId())
        .setAnalyseDate(masterRecord.getAnalyseDate())
        .setState(masterRecord.getState().name())
        .setAnalyseTimeInSeconds(masterRecord.getAnalyseTimeInSeconds())
        .setFailedSummary(masterRecord.getFailedSummary());
  }

  public AnalysisExtendedResponse getExtendedAnalysisResponse(Long id) {
    var recordRepositoryById = masterRecordRepository.findById(id);
    if (recordRepositoryById.isPresent()) {
      var masterRecordEntity = recordRepositoryById.get();

      var analysisExtendedResponse = getAnalysisExtendedResponse(masterRecordEntity);
      if (!ObjectUtils.isEmpty(masterRecordEntity.getRowEntity())) {

        addChildPosts(masterRecordEntity, analysisExtendedResponse);
      }
      return analysisExtendedResponse;
    } else {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, String.format("Id %s is not valid", id));
    }
  }

  private AnalysisExtendedResponse getAnalysisExtendedResponse(
      MasterRecordEntity masterRecordEntity) {
    var analysisExtendedResponse = new AnalysisExtendedResponse();
    analysisExtendedResponse
        .setId(masterRecordEntity.getId())
        .setFailedSummary(masterRecordEntity.getFailedSummary())
        .setState(masterRecordEntity.getState().name());
    return analysisExtendedResponse;
  }

  private void addChildPosts(
      MasterRecordEntity masterRecordEntity, AnalysisExtendedResponse analysisExtendedResponse) {
    analysisExtendedResponse.setChildPosts(
        masterRecordEntity.getRowEntity().stream()
            .filter(rowEntity -> rowEntity.getParentId() == rowEntity.getRowId())
            .map(
                rowEntity ->
                    new AnalysisExtendedResponse.Post()
                        .setId(rowEntity.getRowId())
                        .setScore(rowEntity.getScore())
                        .setChildBodyList(Arrays.asList(rowEntity.getBody().split("\n"))))
            .collect(Collectors.toList()));
  }

  private MasterRecordEntity mapToAnalysis(URL url) {
    return new MasterRecordEntity().setFileUrl(url).setState(State.ENQUEUED);
  }

  public void delete(Long id) {
    try {
      masterRecordRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, String.format("Id %s is not valid", id));
    }
  }
}
