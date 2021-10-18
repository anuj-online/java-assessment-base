package com.group9.assessment.javaassessmentbase.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.assessment.javaassessmentbase.controllers.requests.AnalysisRequest;
import com.group9.assessment.javaassessmentbase.controllers.responses.AnalysisExtendedResponse;
import com.group9.assessment.javaassessmentbase.controllers.responses.AnalysisResponse;
import com.group9.assessment.javaassessmentbase.services.CRUDService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assessment")
@AllArgsConstructor
public class CRUDController {

  // https://archive.org/download/stackexchange/webmasters.stackexchange.com.7z/Posts.xml
  // https://ia600107.us.archive.org/view_archive.php?archive=/27/items/stackexchange/ai.stackexchange.com.7z&file=Posts.xml
  private static final String FILE_NAME = "";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private final CRUDService crudService;

  @ResponseStatus(code = HttpStatus.ACCEPTED)
  @PostMapping(produces = "application/json", consumes = "application/json")
  public long analyse(@RequestBody AnalysisRequest analysisRequest) {
    analysisRequest.validate();
    return crudService.saveAnalysis(analysisRequest.getUrl());
  }

  @GetMapping(value = "/{id}", produces = "application/json")
  public AnalysisResponse getAnalysis(@PathVariable("id") Long id) {
    return crudService.getAnalysis(id);
  }

  @GetMapping(value = "/report/{id}", produces = "application/json")
  public AnalysisExtendedResponse getExtendedAnalysisResponse(@PathVariable("id") Long id) {
    return crudService.getExtendedAnalysisResponse(id);
  }

  @ResponseStatus(code = HttpStatus.ACCEPTED)
  @DeleteMapping(value = "/{id}")
  public void deleteAnalysis(@PathVariable("id") Long id) {
    crudService.delete(id);
  }
}
