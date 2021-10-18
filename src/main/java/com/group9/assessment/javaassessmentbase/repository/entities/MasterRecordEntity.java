package com.group9.assessment.javaassessmentbase.repository.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
public class MasterRecordEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private URL fileUrl;

  private LocalDateTime analyseDate;

  @Column(nullable = false)
  private State state;

  @Nullable private String failedSummary;
  private double analyseTimeInSeconds;

  @JoinColumn(referencedColumnName = "id")
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private AnalysisReportEntity analysisReportEntity;

  @OneToMany(
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true,
      mappedBy = "id")
  private List<RowEntity> rowEntity;

  @Override
  public String toString() {
    return "MasterRecordEntity{"
        + "id="
        + id
        + ", fileUrl="
        + fileUrl
        + ", analyseDate="
        + analyseDate
        + ", state="
        + state
        + ", failedSummary='"
        + failedSummary
        + '\''
        + ", analyseTimeInSeconds="
        + analyseTimeInSeconds
        + '}';
  }
}
