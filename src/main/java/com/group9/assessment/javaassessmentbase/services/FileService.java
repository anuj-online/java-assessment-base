package com.group9.assessment.javaassessmentbase.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.group9.assessment.javaassessmentbase.dto.Row;
import com.group9.assessment.javaassessmentbase.repository.RowRepository;
import com.group9.assessment.javaassessmentbase.repository.entities.MasterRecordEntity;
import com.group9.assessment.javaassessmentbase.repository.entities.RowEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Slf4j
@Service
public class FileService {

  private static final XmlMapper XML_MAPPER;
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new ObjectMapper();
    OBJECT_MAPPER.registerModule(new ParameterNamesModule());
    OBJECT_MAPPER.registerModule(new Jdk8Module());
    OBJECT_MAPPER.registerModule(new JavaTimeModule());

    XML_MAPPER = new XmlMapper();
    XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    XML_MAPPER.registerModule(new ParameterNamesModule());
    XML_MAPPER.registerModule(new Jdk8Module());
    XML_MAPPER.registerModule(new JavaTimeModule());
  }

  private final RowRepository rowRepository;

  public FileService(RowRepository rowRepository) {
    this.rowRepository = rowRepository;
  }

  @Transactional
  public void readFileAndSaveRows(URL url, MasterRecordEntity masterRecordEntity) {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        readLineToRow(inputLine, masterRecordEntity);
      }
    } catch (IOException e) {
      var message = String.format("Exception reading file from url %s", url);
      log.error(message, e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message, e.getCause());
    }
  }

  private void readLineToRow(String inputLine, MasterRecordEntity masterRecordEntity) {
    try {
      var row = parseRow(inputLine);
      var rowEntity = rowEntityWithMasterRecord(masterRecordEntity, row);
      rowRepository.save(rowEntity);
    } catch (JsonProcessingException e) {
      if (!(e.getMessage().contains("EOF") || e.getMessage().contains("</"))) {
        throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, "Exception while parsing file", e.getCause());
      }
    }
  }

  private RowEntity rowEntityWithMasterRecord(MasterRecordEntity masterRecordEntity, Row row) {
    return mapRowToRowEntity(row)
        .setMasterRecordEntity(new MasterRecordEntity().setId(masterRecordEntity.getId()));
  }

  private Row parseRow(String inputLine) throws JsonProcessingException {
    return XML_MAPPER.readValue(inputLine, Row.class);
  }

  private RowEntity mapRowToRowEntity(Row row) {
    return OBJECT_MAPPER.convertValue(row, RowEntity.class);
  }
}
