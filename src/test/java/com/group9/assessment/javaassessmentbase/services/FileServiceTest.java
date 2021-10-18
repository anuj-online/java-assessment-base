package com.group9.assessment.javaassessmentbase.services;

import com.group9.assessment.javaassessmentbase.repository.RowRepository;
import com.group9.assessment.javaassessmentbase.repository.entities.MasterRecordEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

  @InjectMocks private FileService fileService;

  @Mock private RowRepository rowRepository;

  @BeforeAll
  static void setupXmlMapper() {}

  @Test
  void readFileSavesRepo() throws Exception {
    var resource = getClass().getClassLoader().getResource("arabic-posts.xml");
    ;
    System.out.println(resource);
    fileService.readFileAndSaveRows(resource, new MasterRecordEntity());

    verify(rowRepository, times(4)).save(any());
  }

  @Test
  void readFileThrowsExceptionOnInvalildFileLocation() throws Exception {
    var resource =
        new URL("file:/Users/anuj/Downloads/java-assessment-base/build/arabic-posts.xml");

    assertThatThrownBy(() -> fileService.readFileAndSaveRows(resource, new MasterRecordEntity()));
  }
}
