package com.emeritus.collabo.config;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * The Class EventFilterFunction.
 */
public class EventFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

  /** The Constant X_LIVE_EVENTS_WEBHOOK_KEY. */
  private static final String X_LIVE_EVENTS_WEBHOOK_KEY = "X-LIVE-EVENTS-WEBHOOK-KEY";

  /** The Constant CANVAS_EVENTS. */
  private static final String CANVAS_EVENTS = "/canvas-events/";

  /** The event security key. */
  private String eventSecurityKey = null;

  /**
   * Instantiates a new event filter function.
   *
   * @param securityKey the security key
   */
  public EventFilterFunction(String securityKey) {
    this.eventSecurityKey = securityKey;
  }

  /**
   * Filter.
   *
   * @param serverRequest the server request
   * @param handlerFunction the handler function
   * @return the mono
   */
  @Override
  public Mono<ServerResponse> filter(ServerRequest serverRequest,
      HandlerFunction<ServerResponse> handlerFunction) {
    String path = serverRequest.path();
    String key = serverRequest.headers().firstHeader(X_LIVE_EVENTS_WEBHOOK_KEY);

    if (path.startsWith(CANVAS_EVENTS) && (key == null || !key.equals(eventSecurityKey))) {
      return ServerResponse.status(FORBIDDEN).build();
    }
    return handlerFunction.handle(serverRequest);
  }

}
