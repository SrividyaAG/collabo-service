package com.emeritus.collabo.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.emeritus.collabo.entity.RoomInfoEntity;

import reactor.core.publisher.Mono;

/**
 * The Interface RoomInfoRepository.
 */
public interface RoomInfoRepository extends ReactiveCrudRepository<RoomInfoEntity, Integer> {

  /**
   * Find by course id.
   *
   * @param courseId the course id
   * @return the mono
   */
  @Query("select * from collabo.room_info where course_id = $1")
  Mono<RoomInfoEntity> findByCourseId(Integer courseId);

}
