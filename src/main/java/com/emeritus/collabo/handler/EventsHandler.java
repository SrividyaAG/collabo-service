package com.emeritus.collabo.handler;

import java.util.Map;
import java.util.UUID;
import org.bson.Document;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.emeritus.collabo.constant.Constants;
import com.emeritus.collabo.model.EventModel;
import reactor.core.publisher.Mono;

/**
 * The Class EventsHandler.
 */
@Component
public class EventsHandler {

  /** The exclusion handler. */
  @Autowired
  ExclusionHandler exclusionHandler;

  /** The rabbit template. */
  @Autowired
  private RabbitTemplate rabbitTemplate;

  /** The default page size. */
  @Value("${app.pagination.defaultPageSize}")
  private int defaultPageSize;

  /** The create course. */
  @Value("${canvaslms.course.create.event:course_created}")
  private String createCourse;

  /** The create enrollment. */
  @Value("${canvaslms.enrollment.create.event:enrollment_created}")
  private String createEnrollment;

  @Value("${canvaslms.enrollment.update.event:enrollment_updated}")
  private String updateEnrollement;

  /**
   * Post canvas events.
   *
   * @param request the request
   * @return the mono
   */
  @SuppressWarnings("unchecked")
  public Mono<ServerResponse> postCanvasEvents(ServerRequest request) {
    return request.bodyToMono(Document.class).flatMap(payload -> {
      if (exclusionHandler.excludeEvent(payload)) {
        return ServerResponse.noContent().build();
      } else {
        Map<String, String> attributes = payload.get("attributes", Map.class);
        String eventName = attributes.get("event_name");
        EventModel eventModel = new EventModel();
        eventModel.setId(UUID.randomUUID().toString());
        eventModel.setEvent(payload);
        if (eventName.equals(createCourse)) {
          rabbitTemplate.convertAndSend(Constants.RABBIT_EXCHANGE,
              createCourse + Constants.ROUTING_KEY, eventModel);
        } else if (eventName.equals(createEnrollment)) {
          rabbitTemplate.convertAndSend(Constants.RABBIT_EXCHANGE,
              createEnrollment + Constants.ROUTING_KEY, eventModel);
        } else if (eventName.equals(updateEnrollement)) {
          rabbitTemplate.convertAndSend(Constants.RABBIT_EXCHANGE,
                  updateEnrollement + Constants.ROUTING_KEY, eventModel);
        }
        return ServerResponse.ok().body(Mono.just(eventModel), EventModel.class);
      }
    }).switchIfEmpty(ServerResponse.badRequest().bodyValue("Invalid request body"));
  }

  /**
   * Find canvas events.
   *
   * @param request the request
   * @return the mono
   */
  @SuppressWarnings("unused")
  public Mono<ServerResponse> findCanvasEvents(ServerRequest request) {
    return request.bodyToMono(Document.class).flatMap(jsonObject -> {
      int page = jsonObject.getInteger("page", 0);
      int size = jsonObject.getInteger("size", defaultPageSize);
      PageRequest pageable = PageRequest.of(page, size);
      return ServerResponse.ok().body(null, EventModel.class);
    }).switchIfEmpty(ServerResponse.badRequest().bodyValue("Invalid request body"));
  }

  /**
   * Gets the all canvas events.
   *
   * @param request the request
   * @return the all canvas events
   */
  @SuppressWarnings("unused")
  public Mono<ServerResponse> getAllCanvasEvents(ServerRequest request) {
    int page = request.queryParam("page").map(Integer::parseInt).orElse(0);
    int size = request.queryParam("size").map(Integer::parseInt).orElse(defaultPageSize);
    PageRequest pageable = PageRequest.of(page, size);
    return ServerResponse.ok().body(null, EventModel.class);
  }

  /**
   * Check health.
   *
   * @param request the request
   * @return the mono
   */
  public Mono<ServerResponse> checkHealth(ServerRequest request) {
    return ServerResponse.ok().bodyValue("Status: UP");
  }

}
