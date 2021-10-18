package com.group9.assessment.javaassessmentbase.repository.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
public class AnalysisReport {
  private final LocalDateTime firstPost;
  private final LocalDateTime lastPost;
  private final long totalPosts;
  private final long totalAcceptedPosts;
  private final double avgScore;

  public AnalysisReport(
      LocalDateTime firstPost,
      LocalDateTime lastPost,
      long totalPosts,
      long totalAcceptedPosts,
      double avgScore) {
    this.firstPost = firstPost;
    this.lastPost = lastPost;
    this.totalPosts = totalPosts;
    this.totalAcceptedPosts = totalAcceptedPosts;
    this.avgScore = avgScore;
  }
}
