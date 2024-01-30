package com.emeritus.collabo.controller;

import com.emeritus.collabo.model.RemoveUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.emeritus.collabo.model.InviteUserResponse;
import com.emeritus.collabo.model.RoomResponse;
import com.emeritus.collabo.service.MatrixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Mono;

/**
 * The Class RoomController.
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

  private static final String HYPHEN = "-";

  private static final String REGEX = "[^A-Za-z0-9]+";

  /** The Constant CREATE. */
  private static final String CREATE = "create";

  /** The Constant INVITE. */
  private static final String INVITE = "user/invite";

  /** The Constant USER_ID. */
  private static final String USER_ID = "userId";

  /** The Constant ROOM_ID. */
  private static final String ROOM_ID = "roomId";

  /** The Constant COURSE_ID. */
  private static final String COURSE_ID = "courseId";

  /** The Constant COURSE_NAME. */
  private static final String COURSE_NAME = "courseName";


  /** The Constant REMOVE. */
  private static final String REMOVE ="user/remove";

  /** The Constant REASON. */
  public static final String REASON = "reason";

  /** The matrix service. */
  @Autowired
  private MatrixService matrixService;

  /**
   * Creates the room.
   *
   * @param courseName the course name
   * @param courseId the course id
   * @return the mono
   */
  @Operation(summary = "create room", description = "create room")
  @PostMapping(CREATE)
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<RoomResponse> createRoom(
      @Parameter(description = "The course name") @RequestParam(COURSE_NAME) String courseName,
      @Parameter(description = "The course id") @RequestParam(COURSE_ID) Integer courseId) {
    // replace spaces and special characters in course name to -
    courseName = courseName.replaceAll(REGEX, HYPHEN).toLowerCase();
    return matrixService.createRoom(courseName, courseId);
  }

  /**
   * Invite user.
   *
   * @param roomId the room id
   * @param userId the user id
   * @return the mono
   */
  @Operation(summary = "invite user", description = "invite user")
  @PostMapping(INVITE)
  @ResponseStatus(HttpStatus.OK)
  public Mono<InviteUserResponse> inviteUser(
      @Parameter(description = "The room id") @RequestParam(ROOM_ID) String roomId,
      @Parameter(description = "The user id") @RequestParam(USER_ID) String userId) {
    return matrixService.inviteUser(roomId, userId);
  }


  /**
   * Removes the user.
   *
   * @param roomId the room id
   * @param userId the user id
   * @param reason the reason
   * @return the mono
   */
  @Operation(summary = "remove a user", description = "remove a user from room")
  @PostMapping(REMOVE)
  @ResponseStatus(HttpStatus.OK)
  public Mono<RemoveUserResponse> removeUser(
          @Parameter(description = "The room id") @RequestParam(ROOM_ID) String roomId,
          @Parameter(description = "The user id") @RequestParam(USER_ID) String userId,
          @Parameter(description = "Reson for removing") @RequestParam(REASON) String reason) {
    return matrixService.removeUser(roomId, userId, reason);
  }
}
