package com.emeritus.collabo.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.emeritus.collabo.handler.EventsHandler;

/**
 * The Class EventRouterConfig.
 */
@Configuration
public class EventRouterConfig {

  /**
   * Webhook route.
   *
   * @param eventHandler the event handler
   * @return the router function
   */
  @Bean
  public RouterFunction<ServerResponse> webhookRoute(EventsHandler eventHandler) {
    return route(POST("/canvas-events/post"), eventHandler::postCanvasEvents);
  }

  /**
   * Find by query route.
   *
   * @param eventHandler the event handler
   * @return the router function
   */
  @Bean
  public RouterFunction<ServerResponse> findByQueryRoute(EventsHandler eventHandler) {
    return route(POST("/canvas-events/find"), eventHandler::findCanvasEvents);
  }

  /**
   * Gets the all webhook data.
   *
   * @param eventHandler the event handler
   * @return the all webhook data
   */
  @Bean
  public RouterFunction<ServerResponse> getAllWebhookData(EventsHandler eventHandler) {
    return route(GET("/canvas-events/all"), eventHandler::getAllCanvasEvents);
  }

  /**
   * Health check route.
   *
   * @param eventHandler the event handler
   * @return the router function
   */
  @Bean
  public RouterFunction<ServerResponse> healthCheckRoute(EventsHandler eventHandler) {
    return route(GET("/canvas-events/health_check"), eventHandler::checkHealth);
  }

}
