package com.emeritus.collabo.entity;

import java.sql.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Instantiates a new room info entity.
 *
 * @param id the id
 * @param room_id the room id
 * @param room_name the room name
 * @param course_id the course id
 * @param course_name the course name
 * @param created_date the created date
 * @param created_by the created by
 * @param last_modified_date the last modified date
 * @param last_modified_by the last modified by
 */
@AllArgsConstructor

/**
 * Instantiates a new room info entity.
 */
@NoArgsConstructor
@Table(value = "collabo.room_info")

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Builder
public class RoomInfoEntity {

  /** The id. */
  @Id
  private Integer id;

  /** The room id. */
  private String room_id;

  /** The room name. */
  private String room_name;

  /** The course id. */
  private Integer course_id;

  /** The course name. */
  private String course_name;

  /** The created date. */
  private Timestamp created_date;

  /** The created by. */
  private String created_by;

  /** The last modified date. */
  private Timestamp last_modified_date;

  /** The last modified by. */
  private String last_modified_by;

}
