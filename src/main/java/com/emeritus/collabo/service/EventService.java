package com.emeritus.collabo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.emeritus.collabo.model.EventModel;
import com.emeritus.collabo.repository.EventsRepository;
import reactor.core.publisher.Mono;

/**
 * The Class EventService.
 */
@Service
public class EventService {

  /** The events repository. */
  @Autowired
  private EventsRepository eventsRepository;

  /**
   * Process canvas events.
   *
   * @param eventModel the event model
   * @return the mono
   */
  public Mono<EventModel> processCanvasEvents(EventModel eventModel) {
    return eventsRepository.save(eventModel);
  }

  /**
   * Gets the all canvas events.
   *
   * @param pageable the pageable
   * @return the all canvas events
   */
  public Mono<Page<EventModel>> getAllCanvasEvents(Pageable pageable) {
    return eventsRepository.findAll(pageable).collectList().zipWith(eventsRepository.eventCount())
        .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
  }

  /**
   * Find by query.
   *
   * @param jsonQuery the json query
   * @param jsonFilter the json filter
   * @param pageable the pageable
   * @return the mono
   */
  public Mono<Page<EventModel>> findByQuery(String jsonQuery, String jsonFilter,
      Pageable pageable) {
    return eventsRepository.findByQuery(jsonQuery, jsonFilter, pageable).collectList()
        .zipWith(eventsRepository.eventCount(jsonQuery, jsonFilter))
        .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
  }

}
