package com.group9.assessment.javaassessmentbase.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MapperConfiguration {

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    var objectMapper = new ObjectMapper();
    objectMapper.registerModule(new ParameterNamesModule());
    objectMapper.registerModule(new Jdk8Module());
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }
}
