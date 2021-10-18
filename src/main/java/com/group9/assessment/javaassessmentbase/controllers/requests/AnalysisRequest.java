package com.group9.assessment.javaassessmentbase.controllers.requests;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.net.URL;

@Data
@Accessors(chain = true)
public class AnalysisRequest {

  private URL url;

  public void validate() {
    if (url == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not valid url");
    }
  }
}
