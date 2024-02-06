package com.emeritus.collabo.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.emeritus.collabo.repository.EventsRepository;
import reactor.core.publisher.Mono;

/**
 * The Class OldEventsCleanUpScheduler.
 */
@Component
public class OldEventsCleanUpScheduler {

  /** The events repository. */
  @Autowired
  EventsRepository eventsRepository;

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(OldEventsCleanUpScheduler.class);

  /**
   * Delete old events.
   */
  @Scheduled(cron = "${scheduler.cron}")
  public void deleteOldEvents() {
    eventsRepository.deleteOlderEvents()
        .doOnSubscribe(
            subscription -> logger.info("Started deleting events older than configured duration"))
        .doOnSuccess(unused -> logger.info("Completed deleting old events"))
        .doOnError(e -> logger.error("Error during deletion of old events", e))
        .onErrorResume(e -> Mono.empty())
        .subscribe(result -> logger.info("Successfully completed deletion of old events"),
            error -> logger.error("Error occurred: ", error),
            () -> logger.info("Scheduled task deleteOldEvents finished"));
  }
}
