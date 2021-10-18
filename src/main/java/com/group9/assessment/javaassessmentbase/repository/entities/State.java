package com.group9.assessment.javaassessmentbase.repository.entities;

public enum State {
  ENQUEUED("enqueued"),
  ANALYSING("analysing"),
  FINISHED("finished"),
  DELETED("deleted"),
  FAILED("failed");

  private String state;

  State(String state) {
    this.state = state;
  }
}
