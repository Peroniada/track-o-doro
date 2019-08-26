package com.sperek.application;

public class ApplicationRunner implements Runnable {

  private JavalinConfig javalinConfig;
  private JsonMapperConfig jsonMapperConfig;

  ApplicationRunner(JavalinConfig javalinConfig,
      JsonMapperConfig jsonMapperConfig) {
    this.javalinConfig = javalinConfig;
    this.jsonMapperConfig = jsonMapperConfig;
  }

  @Override
  public void run() {
    javalinConfig.run();
    jsonMapperConfig.run();
  }

}
