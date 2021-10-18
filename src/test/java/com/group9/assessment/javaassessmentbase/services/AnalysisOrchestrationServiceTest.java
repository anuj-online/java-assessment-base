package com.group9.assessment.javaassessmentbase.services;

import com.group9.assessment.javaassessmentbase.repository.MasterRecordRepository;
import com.group9.assessment.javaassessmentbase.repository.RowRepository;
import com.group9.assessment.javaassessmentbase.repository.entities.MasterRecordEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalysisOrchestrationServiceTest {

  @InjectMocks private AnalysisOrchestrationService analysisOrchestrationService;

  @Mock private MasterRecordRepository masterRecordRepository;
  @Mock private FileService fileService;
  @Mock private RowRepository rowRepository;

  @Test
  void whenMasterRecordNotPresentDoNothing() {

    when(masterRecordRepository.findById(any())).thenReturn(Optional.empty());
    analysisOrchestrationService.startAnalysis(1L);

    verify(fileService, never()).readFileAndSaveRows(any(), any());
    verify(rowRepository, never()).save(any());
  }

  @Test
  void whenFileServiceThrowsExceptionSaveFailedSummary() {

    when(masterRecordRepository.findById(any())).thenReturn(Optional.of(new MasterRecordEntity()));
    when(masterRecordRepository.save(any())).thenReturn(new MasterRecordEntity());
    var error = "error";
    doThrow(new RuntimeException(error)).when(fileService).readFileAndSaveRows(any(), any());

    var masterRecordEntityArgumentCaptor = ArgumentCaptor.forClass(MasterRecordEntity.class);
    analysisOrchestrationService.startAnalysis(1L);

    verify(fileService).readFileAndSaveRows(any(), any());
    verify(rowRepository, never()).save(any());

    verify(masterRecordRepository, times(2)).save(masterRecordEntityArgumentCaptor.capture());

    assertThat(error).isEqualTo(masterRecordEntityArgumentCaptor.getValue().getFailedSummary());
  }
}
