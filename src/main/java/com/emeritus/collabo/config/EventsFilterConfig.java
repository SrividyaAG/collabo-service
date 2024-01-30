package com.emeritus.collabo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * The Class EventsFilterConfig.
 */
@Configuration
public class EventsFilterConfig {

  /** The Constant CANVAS_EVENTS. */
  private static final String CANVAS_EVENTS = "/canvas-events/";

  /** The Constant X_LIVE_EVENTS_WEBHOOK_KEY. */
  private static final String X_LIVE_EVENTS_WEBHOOK_KEY = "X-LIVE-EVENTS-WEBHOOK-KEY";

  /** The expected key. */
  @Value("${canvaslms.event.security.key}")
  private String expectedKey;

  /**
   * Canvas events filter.
   *
   * @return the web filter
   */
  @Bean
  @Order(-1)
  public WebFilter canvasEventsFilter() {
    return (ServerWebExchange exchange, WebFilterChain chain) -> {
      String path = exchange.getRequest().getURI().getPath();
      String key = exchange.getRequest().getHeaders().getFirst(X_LIVE_EVENTS_WEBHOOK_KEY);

      if (path.startsWith(CANVAS_EVENTS) && (key == null || !key.equals(expectedKey))) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return Mono.empty();
      }
      return chain.filter(exchange);
    };
  }

}
