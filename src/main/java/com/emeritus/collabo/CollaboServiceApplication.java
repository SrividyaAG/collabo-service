package com.emeritus.collabo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Class CollaboServiceApplication.
 */
@SpringBootApplication
public class CollaboServiceApplication {

  /** The logger. */
  private static Logger logger = LoggerFactory.getLogger(CollaboServiceApplication.class);

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(CollaboServiceApplication.class, args);
    logger.info("Collabo Rabbit MQ Service Running!");
  }
}
