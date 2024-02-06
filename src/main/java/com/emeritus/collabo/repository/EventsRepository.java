package com.emeritus.collabo.repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.emeritus.collabo.model.EventModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The Class EventsRepository.
 */
@Component

/**
 * Instantiates a new events repository.
 *
 * @param template the template
 */
@RequiredArgsConstructor
public class EventsRepository {

  /** The template. */
  private final ReactiveMongoTemplate template;

  @Value("${cleanup.events.daysPast:30}")
  private long daysToKeep;

  /**
   * Save.
   *
   * @param event the event
   * @return the mono
   */
  public Mono<EventModel> save(EventModel event) {
    return template.save(event);
  }

  /**
   * Find all.
   *
   * @param pageable the pageable
   * @return the flux
   */
  public Flux<EventModel> findAll(Pageable pageable) {
    Query query = new Query().with(pageable);
    return template.find(query, EventModel.class, "events");
  }

  /**
   * Find by query.
   *
   * @param jsonQuery the json query
   * @param jsonFilter the json filter
   * @param pageable the pageable
   * @return the flux
   */
  public Flux<EventModel> findByQuery(final String jsonQuery, final String jsonFilter,
      Pageable pageable) {
    Query query = new BasicQuery(jsonQuery, jsonFilter).with(pageable);
    return template.find(query, EventModel.class, "events");
  }

  /**
   * Event count.
   *
   * @return the mono
   */
  public Mono<Long> eventCount() {
    return template.count(new Query(), "events");
  }

  /**
   * Event count.
   *
   * @param jsonQuery the json query
   * @param jsonFilter the json filter
   * @return the mono
   */
  public Mono<Long> eventCount(final String jsonQuery, final String jsonFilter) {
    Query query = new BasicQuery(jsonQuery, jsonFilter);
    return template.count(query, "events");
  }

  /**
   * Delete events older than 30 days.
   *
   * @return the mono
   */
  public Mono<Void> deleteOlderEvents() {
    Instant threshold = Instant.now().minus(daysToKeep, ChronoUnit.DAYS);
    Query query = new Query(Criteria.where("event.event_time").lt(threshold.toString()));
    return template.remove(query, EventModel.class, "events").then();
  }

}
