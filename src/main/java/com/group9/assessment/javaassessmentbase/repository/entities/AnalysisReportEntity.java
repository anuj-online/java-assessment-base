package com.group9.assessment.javaassessmentbase.repository.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
public class AnalysisReportEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private long id;

  private LocalDateTime firstPost;
  private LocalDateTime lastPost;
  private long totalPosts;
  private long totalAcceptedPosts;
  private double avgScore;
}
