package com.group9.assessment.javaassessmentbase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
class JavaAssessmentBaseApplicationTests {

  @Bean
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    return new ThreadPoolTaskExecutor();
  }
}
