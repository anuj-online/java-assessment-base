package com.group9.assessment.javaassessmentbase.controllers.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AnalysisExtendedResponse {
  private long id;
  private String state;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String failedSummary;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<Post> childPosts;

  @Data
  @Accessors(chain = true)
  public static class Post {
    private long id;
    private long score;
    private List<String> childBodyList;
  }
}
