package com.group9.assessment.javaassessmentbase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@Slf4j
@EnableScheduling
public class JavaAssessmentBaseApplication {
  public static void main(String[] args) {
    SpringApplication.run(JavaAssessmentBaseApplication.class, args);
  }

  public static void logMemory() {
    log.info("Max Memory: {} Mb", Runtime.getRuntime().maxMemory() / 1048576);
    log.info("Total Memory: {} Mb", Runtime.getRuntime().totalMemory() / 1048576);
    log.info("Free Memory: {} Mb", Runtime.getRuntime().freeMemory() / 1048576);
  }
}
