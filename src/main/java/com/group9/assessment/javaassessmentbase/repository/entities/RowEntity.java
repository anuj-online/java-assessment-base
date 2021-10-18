package com.group9.assessment.javaassessmentbase.repository.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
public class RowEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private long rowId;

  private String PostTypeId;

  private int acceptedAnswerId;

  private LocalDateTime creationDate;

  private int parentId;

  private int score;

  @Column(length = Integer.MAX_VALUE)
  private String body;

  private int viewCount;

  private String ownerUserId;

  private LocalDateTime lastActivityDate;

  private String title;

  private int commentCount;

  @JoinColumn(name = "master_record_id", updatable = false)
  @ManyToOne(fetch = FetchType.EAGER)
  private MasterRecordEntity masterRecordEntity;
}
