package com.group9.assessment.javaassessmentbase.services;

import com.group9.assessment.javaassessmentbase.TestUtils;
import com.group9.assessment.javaassessmentbase.repository.MasterRecordRepository;
import com.group9.assessment.javaassessmentbase.repository.entities.MasterRecordEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CRUDServiceTest {

  @InjectMocks private CRUDService crudService;
  @Mock private MasterRecordRepository masterRecordRepository;
  @Mock private AnalysisOrchestrationService analysisOrchestrationService;

  @Test
  void saveAnalysis() throws MalformedURLException {

    long expectedId = 1L;
    when(masterRecordRepository.save(any())).thenReturn(new MasterRecordEntity().setId(expectedId));

    var actualId = crudService.saveAnalysis(new URL("http://local.com"));

    assertThat(expectedId).isEqualTo(actualId);
  }

  @Test
  void getAnalysisMapsValues() throws Exception {

    var expectedId = 1L;
    var expectedMasterRecord = TestUtils.getMasterRecord(1L);
    when(masterRecordRepository.findById(expectedId)).thenReturn(Optional.of(expectedMasterRecord));

    var actualResponse = crudService.getAnalysis(expectedId);

    assertThat(expectedId).isEqualTo(actualResponse.getId());
    assertThat(expectedMasterRecord.getAnalyseDate()).isEqualTo(actualResponse.getAnalyseDate());
    assertThat(expectedMasterRecord.getState().name()).isEqualTo(actualResponse.getState());
    assertThat(actualResponse.getDetailsResponse().getAvgScore())
        .isEqualTo(expectedMasterRecord.getAnalysisReportEntity().getAvgScore());
    assertThat(actualResponse.getDetailsResponse().getFirstPost())
        .isEqualTo(expectedMasterRecord.getAnalysisReportEntity().getFirstPost());

    assertThat(actualResponse.getDetailsResponse().getLastPost())
        .isEqualTo(expectedMasterRecord.getAnalysisReportEntity().getLastPost());
    assertThat(actualResponse.getDetailsResponse().getTotalAcceptedPosts())
        .isEqualTo(expectedMasterRecord.getAnalysisReportEntity().getTotalAcceptedPosts());

    assertThat(actualResponse.getFailedSummary())
        .isEqualTo(expectedMasterRecord.getFailedSummary());
  }

  @Test
  void getAnalysisThrowsExceptionWhenAnalysisIdNotExistent() {
    when(masterRecordRepository.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> crudService.getAnalysis(1L))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  void getExtendedAnalysisResponse() throws Exception {

    var expectedAnalyseId = 1L;

    var expectedMasterRecord = TestUtils.getMasterRecord(1L);
    when(masterRecordRepository.findById(expectedAnalyseId))
        .thenReturn(Optional.of(expectedMasterRecord));

    var actualResponse = crudService.getExtendedAnalysisResponse(expectedAnalyseId);

    assertThat(expectedAnalyseId).isEqualTo(actualResponse.getId());

    assertThat(expectedMasterRecord.getState().name()).isEqualTo(actualResponse.getState());

    assertThat(actualResponse.getFailedSummary())
        .isEqualTo(expectedMasterRecord.getFailedSummary());

    assertThat(expectedMasterRecord.getRowEntity().stream().findFirst().get().getBody())
        .isEqualTo(
            actualResponse.getChildPosts().stream()
                .findFirst()
                .get()
                .getChildBodyList()
                .stream()
                .findFirst()
                .get());
  }

  @Test
  void getExtendedAnalysisThrowsExceptionWhenAnalysisIdNotExistent() {
    when(masterRecordRepository.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> crudService.getExtendedAnalysisResponse(1L))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  void deleteAnalysis() {

    doNothing().when(masterRecordRepository).deleteById(any());

    try {
      crudService.delete(1L);
    } catch (Exception e) {
      fail("Does not expect any exception to be thrown here");
    }
  }

  @Test
  void deleteAnalysisThrowsException() {

    doThrow(EmptyResultDataAccessException.class).when(masterRecordRepository).deleteById(any());

    try {
      crudService.delete(1L);
      fail("Does not expect any exception to be thrown here");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(ResponseStatusException.class);
      var e1 = (ResponseStatusException) e;
      assertThat(e1.getRawStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
  }
}
