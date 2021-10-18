package com.group9.assessment.javaassessmentbase.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AnalysisResponse {
  private long id;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime analyseDate;

  private String state;
  private double analyseTimeInSeconds;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String failedSummary;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private DetailsResponse detailsResponse;

  @Data
  @Accessors(chain = true)
  public static class DetailsResponse {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime firstPost;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastPost;

    private long totalPosts;
    private long totalAcceptedPosts;
    private double avgScore;
  }
}
