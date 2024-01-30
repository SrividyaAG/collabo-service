package com.emeritus.collabo.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The Class CollaboConfig.
 */
@Configuration
public class CollaboConfig {

  /** The matrix base url. */
  @Value("${matrix.baseurl}")
  private String matrixBaseUrl;

  /**
   * Model mapper bean.
   *
   * @return the model mapper
   */
  @Bean
  public ModelMapper modelMapperBean() {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper;
  }

  /**
   * Web client.
   *
   * @return the web client
   */
  @Bean
  public WebClient webClient() {
    return WebClient.builder().baseUrl(matrixBaseUrl).build();
  }

}
