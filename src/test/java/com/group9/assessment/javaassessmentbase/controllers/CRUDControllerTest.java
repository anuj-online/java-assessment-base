package com.group9.assessment.javaassessmentbase.controllers;

import com.group9.assessment.javaassessmentbase.TestUtils;
import com.group9.assessment.javaassessmentbase.controllers.requests.AnalysisRequest;
import com.group9.assessment.javaassessmentbase.repository.AnalysisRepository;
import com.group9.assessment.javaassessmentbase.repository.MasterRecordRepository;
import com.group9.assessment.javaassessmentbase.repository.RowRepository;
import com.group9.assessment.javaassessmentbase.services.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class CRUDControllerTest {

  @Autowired ThreadPoolTaskExecutor threadPoolTaskExecutor;
  @Autowired private CRUDController crudController;
  @Autowired private MasterRecordRepository masterRecordRepository;
  @SpyBean private FileService fileService;
  @Autowired private RowRepository rowRepository;
  @Autowired private AnalysisRepository analysisRepository;

  private static Stream<Arguments> invalidFiles() {
    return Stream.of(Arguments.of("aaa.aaa", "no-file.xml"));
  }

  @BeforeEach
  void cleanDb() {
    rowRepository.deleteAll();
    masterRecordRepository.deleteAll();
    analysisRepository.deleteAll();
  }

  @Test
  void verifyProcessResultsWhenRequestIsValid() throws Exception {

    long analyseId =
        crudController.analyse(
            new AnalysisRequest()
                .setUrl(getClass().getClassLoader().getResource("arabic-posts.xml")));
    threadPoolTaskExecutor.getThreadPoolExecutor().awaitTermination(5, TimeUnit.SECONDS);

    var actualMasterEntity = masterRecordRepository.findById(analyseId);
    assertThat(actualMasterEntity).isPresent();
    assertThat(analyseId).isEqualTo(actualMasterEntity.get().getId());
    assertThat(rowRepository.findAll().size()).isEqualTo(4);
  }

  @ParameterizedTest
  @MethodSource("invalidFiles")
  void whenInvalidURLThrowsException(String invalidFileName) {
    assertThatThrownBy(
            () ->
                crudController.analyse(
                    new AnalysisRequest()
                        .setUrl(getClass().getClassLoader().getResource(invalidFileName))))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  void whenRowIdAndParentIdAreSameReturnsChildBody() throws Exception {
    var analyseId =
        crudController.analyse(
            new AnalysisRequest()
                .setUrl(getClass().getClassLoader().getResource("arabic-posts.xml")));

    threadPoolTaskExecutor.getThreadPoolExecutor().awaitTermination(5, TimeUnit.SECONDS);

    var extendedAnalysisResponse = crudController.getExtendedAnalysisResponse(analyseId);
    assertThat(extendedAnalysisResponse.getChildPosts()).isNotNull();
  }

  @Test
  void returnsStatusFailedWhenErrorInProcessing() throws Exception {
    var exceptionMessage = "some issue";
    doThrow(new RuntimeException(exceptionMessage))
        .when(fileService)
        .readFileAndSaveRows(any(), any());
    var analyse =
        crudController.analyse(
            new AnalysisRequest()
                .setUrl(getClass().getClassLoader().getResource("arabic-posts.xml")));
    threadPoolTaskExecutor.getThreadPoolExecutor().awaitTermination(5, TimeUnit.SECONDS);

    var analysis = crudController.getAnalysis(analyse);
    assertThat(analysis.getFailedSummary()).isEqualTo(exceptionMessage);
  }

  @Test
  void deletesAnalysisDeletesAllDataForID() throws Exception {
    var masterRecordEntity = TestUtils.getMasterRecord(1L);
    masterRecordEntity = masterRecordRepository.save(masterRecordEntity);

    // pre verification to deny after controller method execution
    assertThat(masterRecordRepository.existsById(masterRecordEntity.getId())).isTrue();
    assertThat(rowRepository.findAll()).isNotEmpty();
    assertThat(analysisRepository.findAll()).isNotEmpty();

    crudController.deleteAnalysis(2L);

    // verifying entity is deleted
    assertThat(masterRecordRepository.existsById(masterRecordEntity.getId())).isFalse();
    assertThat(rowRepository.findAll()).isEmpty();
    assertThat(analysisRepository.findAll()).isEmpty();
  }

  @Test
  void deletesAnalysisThrowsExceptionWhenInvalidID() {

    assertThatThrownBy(() -> crudController.deleteAnalysis(2L))
        .isInstanceOf(ResponseStatusException.class)
        .extracting("status")
        .isEqualTo(HttpStatus.NOT_FOUND);
  }
}
