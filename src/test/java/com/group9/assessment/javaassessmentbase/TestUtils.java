package com.group9.assessment.javaassessmentbase;

import com.group9.assessment.javaassessmentbase.repository.entities.AnalysisReportEntity;
import com.group9.assessment.javaassessmentbase.repository.entities.MasterRecordEntity;
import com.group9.assessment.javaassessmentbase.repository.entities.RowEntity;
import com.group9.assessment.javaassessmentbase.repository.entities.State;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public final class TestUtils {

  public static MasterRecordEntity getMasterRecord(long l) throws MalformedURLException {
    return new MasterRecordEntity()
        .setId(l)
        .setFileUrl(new URL("http://google.com"))
        .setAnalyseDate(LocalDateTime.now())
        .setState(State.FINISHED)
        .setFailedSummary("some-message")
        .setAnalyseTimeInSeconds(5.0)
        .setAnalysisReportEntity(getAnalysisReportEntity())
        .setRowEntity(getRowEntitties());
  }

  private static List<RowEntity> getRowEntitties() {
    return List.of(
        new RowEntity()
            .setScore(1)
            .setTitle("title")
            .setCreationDate(LocalDateTime.now())
            .setLastActivityDate(LocalDateTime.now())
            .setAcceptedAnswerId(1)
            .setBody("body")
            .setCommentCount(1)
            .setId(1)
            .setOwnerUserId("owner-user-id")
            .setParentId(1)
            .setPostTypeId("postTypeId")
            .setRowId(1)
            .setViewCount(1));
  }

  private static AnalysisReportEntity getAnalysisReportEntity() {
    return new AnalysisReportEntity()
        .setId(2L)
        .setFirstPost(LocalDateTime.of(LocalDate.of(2001, 01, 01), LocalTime.MIDNIGHT))
        .setLastPost(LocalDateTime.of(LocalDate.of(2002, 01, 01), LocalTime.MIDNIGHT))
        .setTotalPosts(10L)
        .setTotalAcceptedPosts(5L)
        .setAvgScore(3.2);
  }
}
