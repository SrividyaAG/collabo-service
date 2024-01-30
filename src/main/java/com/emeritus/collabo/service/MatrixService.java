package com.emeritus.collabo.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;

import com.emeritus.collabo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException.Unauthorized;
import com.emeritus.collabo.entity.RoomInfoEntity;
import com.emeritus.collabo.repository.RoomInfoRepository;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * The Class MatrixService.
 */
@Service
public class MatrixService {

  /** The Constant BEARER. */
  private static final String BEARER = "Bearer ";

  /** The Constant WELCOME_TO_THE_TEAM. */
  private static final String WELCOME_TO_THE_TEAM = "Welcome to the team!";

  /** The Constant M_ID_USER. */
  private static final String M_ID_USER = "m.id.user";

  /** The Constant M_LOGIN_PASSWORD. */
  private static final String M_LOGIN_PASSWORD = "m.login.password";

  /** The Constant AUTHORIZATION. */
  private static final String AUTHORIZATION = "Authorization";

  /** The Constant PRIVATE_CHAT. */
  private static final String PRIVATE_CHAT = "private_chat";

  /** The Constant HYPHEN. */
  private static final String HYPHEN = "-";

  /** The Constant LOGIN. */
  private static final String LOGIN = "/login";

  /** The Constant CREATE_ROOM. */
  private static final String CREATE_ROOM = "/createRoom";

  /** The Constant INVITE. */
  private static final String INVITE = "/rooms/{roomId}/invite";

  /** The Constant REMOVE. */
  private static final String REMOVE = "/rooms/{roomId}/kick";
  /** The logger. */
  private Logger logger = LoggerFactory.getLogger(MatrixService.class);

  /** The user name. */
  @Value("${matrix.username}")
  private String userName;

  /** The password. */
  @Value("${matrix.password}")
  private String password;

  /** The m access token. */
  @Value("${matrix.accesstoken:syt_YWRtaW4_arNBIxilWKuJTNCsmUPx_1cW3op}")
  private String m_accessToken;

  /** The web client. */
  @Autowired
  private WebClient webClient;

  /** The room info repository. */
  @Autowired
  private RoomInfoRepository roomInfoRepository;

  /**
   * Creates the room.
   *
   * @param courseName the course name
   * @param courseId the course id
   * @return the mono
   */
  public Mono<RoomResponse> createRoom(String courseName, Integer courseId) {
    return roomInfoRepository.findByCourseId(courseId)
        .flatMap(__ -> Mono.just(RoomResponse.builder()
            .message("Room already exists for this courseId: " + courseId).build()))
        .switchIfEmpty(Mono.defer(() -> createRoomServiceCall(courseName, courseId)))
        .cast(RoomResponse.class);
  }

  /**
   * Creates the room service call.
   *
   * @param courseName the course name
   * @param courseId the course id
   * @return the mono
   */
  private Mono<RoomResponse> createRoomServiceCall(String courseName, Integer courseId) {
    RoomRequest roomRequest = getRoomRequest(courseName, courseId);
    Date date = new Date();
    RoomInfoEntity roomInfoEntity = RoomInfoEntity.builder()
        .room_name(roomRequest.getRoomAliasName()).course_id(courseId).course_name(courseName)
        .created_by(userName).last_modified_by(userName).created_date(new Timestamp(date.getTime()))
        .last_modified_date(new Timestamp(date.getTime())).build();
    Mono<RoomResponse> roomResponse = createRoom(roomRequest);
    return roomResponse.doOnSuccess(createdRoom -> {
      createdRoom.setMessage("Room created successfully!");
      logger.info("Room created: " + createdRoom);
      roomInfoEntity.setRoom_id(createdRoom.getRoomId());
      roomInfoRepository.save(roomInfoEntity).subscribe();
    }).doOnError(error -> {
      logger.error("Error creating room: " + error.getMessage(), error);
      if (error instanceof Unauthorized) {
        Mono<AccessTokenResponse> accessToken = getAccessToken();
        accessToken.subscribe(accessTokenResponse -> {
          logger.info("AccessToken created: " + accessTokenResponse);
          setAccessToken(accessTokenResponse);
        });
      }
    }).retryWhen(Retry.backoff(1, Duration.ofSeconds(50))
        .filter(throwable -> throwable instanceof Unauthorized));
  }

  /**
   * Gets the room request.
   *
   * @param courseName the course name
   * @param courseId the course id
   * @return the room request
   */
  private RoomRequest getRoomRequest(String courseName, Integer courseId) {
    RoomRequest room = new RoomRequest();
    room.setPreset(PRIVATE_CHAT);
    room.setRoomAliasName(courseName + HYPHEN + courseId);
    room.setName(room.getRoomAliasName());
    room.setTopic(room.getRoomAliasName());
    CreationContent creationContent = new CreationContent();
    creationContent.setFederate(false);
    room.setCreationContent(creationContent);
    return room;
  }

  /**
   * Creates the room.
   *
   * @param room the room
   * @return the mono
   */
  private Mono<RoomResponse> createRoom(RoomRequest room) {
    Mono<RoomResponse> response = webClient.post().uri(CREATE_ROOM).headers(httpHeaders -> {
      httpHeaders.add(AUTHORIZATION, BEARER + m_accessToken);
    }).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(room), RoomRequest.class).retrieve().bodyToMono(RoomResponse.class);
    return response;
  }

  /**
   * Gets the access token.
   *
   * @return the access token
   */
  public Mono<AccessTokenResponse> getAccessToken() {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setType(M_LOGIN_PASSWORD);
    loginRequest.setPassword(password);
    Identifier identifier = Identifier.builder().type(M_ID_USER).user(userName).build();
    loginRequest.setIdentifier(identifier);
    Mono<AccessTokenResponse> response =
        webClient.post().uri(LOGIN).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).body(Mono.just(loginRequest), LoginRequest.class)
            .retrieve().bodyToMono(AccessTokenResponse.class);
    return response;
  }

  /**
   * Sets the access token.
   *
   * @param accessToken the new access token
   */
  private void setAccessToken(AccessTokenResponse accessToken) {
    logger.info("Created matrix room" + accessToken);
    m_accessToken = accessToken.getAccessToken();
  }

  /**
   * Invite user.
   *
   * @param roomId the room id
   * @param userId the user id
   * @return the mono
   */
  public Mono<InviteUserResponse> inviteUser(String roomId, String userId) {
    InviteUserRequest inviteUserRequest = getInviteRequest(userId);
    Mono<InviteUserResponse> inviteUserResponse = createInvite(roomId, inviteUserRequest);
    return inviteUserResponse.doOnSuccess(userInvited -> {
      logger.info("User invited: " + userInvited);
    }).doOnError(error -> {
      logger.error("Error inviting user: " + error.getMessage(), error);
      if (error instanceof Unauthorized) {
        Mono<AccessTokenResponse> accessToken = getAccessToken();
        accessToken.subscribe(accessTokenResponse -> {
          logger.info("AccessToken created: " + accessTokenResponse);
          setAccessToken(accessTokenResponse);
        });
      }
    }).retryWhen(Retry.backoff(1, Duration.ofSeconds(50))
        .filter(throwable -> throwable instanceof Unauthorized));
  }

  /**
   * Gets the invite request.
   *
   * @param userId the user id
   * @return the invite request
   */
  private InviteUserRequest getInviteRequest(String userId) {
    InviteUserRequest inviteUserRequest = new InviteUserRequest();
    inviteUserRequest.setUserId(userId);
    inviteUserRequest.setReason(WELCOME_TO_THE_TEAM);
    return inviteUserRequest;
  }

  /**
   * Creates the invite.
   *
   * @param roomId the room id
   * @param inviteRequest the invite request
   * @return the mono
   */
  private Mono<InviteUserResponse> createInvite(String roomId, InviteUserRequest inviteRequest) {
    Mono<InviteUserResponse> response = webClient.post()
        .uri(uriBuilder -> uriBuilder.path(INVITE).build(roomId)).headers(httpHeaders -> {
          httpHeaders.add(AUTHORIZATION, BEARER + m_accessToken);
        }).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(inviteRequest), InviteUserRequest.class).retrieve()
        .bodyToMono(InviteUserResponse.class);
    return response;
  }

  /**
   * Removes the user.
   *
   * @param roomId the room id
   * @param userId the user id
   * @param reason the reason
   * @return the mono
   */
  public Mono<RemoveUserResponse> removeUser(String roomId, String userId, String reason) {
    RemoveUserRequest removeUserRequest = new RemoveUserRequest(userId, reason);
    Mono<RemoveUserResponse> removeUserResponse = removeUserRequest(roomId, removeUserRequest);
    return removeUserResponse.doOnSuccess(userRemove -> {
      logger.info("User Remover: {}", userRemove);
      logger.info("User Remove successfully");
    }).doOnError(error -> {
      logger.error("Error removing user: " + error.getMessage(), error);
      if (error instanceof Unauthorized) {
        Mono<AccessTokenResponse> accessToken = getAccessToken();
        accessToken.subscribe(accessTokenResponse -> {
          logger.info("AccessToken created: {}", accessTokenResponse);
          setAccessToken(accessTokenResponse);
        });
      }
    }).retryWhen(Retry.backoff(1, Duration.ofSeconds(50))
            .filter(throwable -> throwable instanceof Unauthorized));
  }

  /**
   * Removes the user request.
   *
   * @param roomId the room id
   * @param removeUserRequest the remove user request
   * @return the mono
   */
  private Mono<RemoveUserResponse> removeUserRequest(String roomId,
                                                     RemoveUserRequest removeUserRequest) {
    return webClient.post().uri(uriBuilder -> uriBuilder.path(REMOVE).build(roomId))
            .headers(httpHeaders -> {
              httpHeaders.add(AUTHORIZATION, BEARER + m_accessToken);
            }).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(removeUserRequest), RemoveUserRequest.class).retrieve()
            .bodyToMono(RemoveUserResponse.class);
  }

}
