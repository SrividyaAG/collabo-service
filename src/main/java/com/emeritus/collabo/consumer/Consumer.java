package com.emeritus.collabo.consumer;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.emeritus.collabo.controller.RoomController;
import com.emeritus.collabo.model.EventModel;
import com.emeritus.collabo.repository.RoomInfoRepository;
import com.emeritus.collabo.service.CanvasUserService;

/**
 * The Class Consumer.
 */
@Component
public class Consumer {

  /** The Constant NAME. */
  private static final String NAME = "name";

  /** The Constant USER_ID. */
  private static final String USER_ID = "user_id";

  /** The Constant EMPTY. */
  private static final String EMPTY = "";

  /** The Constant COURSE_ID. */
  private static final String COURSE_ID = "course_id";

  /** The Constant BODY. */
  private static final String BODY = "body";

  /** The Constant ACCOUNT_ID. */
  private static final String ACCOUNT_ID = "1";

  /** The Constant REASON. */
  private static final String REASON = "Unenrollement";

  /** The Constant WORKFLOWSTATE. */
  private static final String WORKFLOWSTATE = "workflow_state";

  /** The Constant DELETED. */
  private static final String DELETED = "deleted";

  /** The Constant INACTIVE. */
  private static final String INACTIVE = "inactive";

  /** The logger. */
  private Logger logger = LoggerFactory.getLogger(Consumer.class);

  /** The room controller. */
  @Autowired
  private RoomController roomController;

  /** The room info repository. */
  @Autowired
  private RoomInfoRepository roomInfoRepository;

  @Autowired
  private CanvasUserService canvasUserService;

  /**
   * Consume create course.
   *
   * @param eventModel the event model
   */
  @SuppressWarnings("unchecked")
  @RabbitListener(queues = {"${canvaslms.course.create.event}"})
  public void consumeCreateCourse(EventModel eventModel) {
    logger.info("Message Received from create course: " + eventModel);
    Document payload = eventModel.getEvent();
    Map<String, String> body = payload.get(BODY, Map.class);
    Integer courseId = Integer.parseInt(body.get(COURSE_ID).replaceFirst(ACCOUNT_ID, EMPTY));
    String courseName = body.get(NAME);
    roomController.createRoom(courseName, courseId)
        .subscribe(room -> logger.info("RoomResponse " + room));
    logger.info("Event - create room");
  }

  /**
   * Consume create enrollment.
   *
   * @param eventModel the event model
   */
  @SuppressWarnings("unchecked")
  @RabbitListener(queues = {"${canvaslms.enrollment.create.event}"})
  public void consumeCreateEnrollment(EventModel eventModel) {
    logger.info("Message Received from create enrollment: " + eventModel);

    Document payload = eventModel.getEvent();
    Map<String, String> body = payload.get(BODY, Map.class);
    String userId = body.get(USER_ID);
    // calling canvas to get userName
    String userName = canvasUserService.getUserName(userId);
    if (userName != null) {
      Integer courseId = Integer.parseInt(body.get(COURSE_ID).replaceFirst(ACCOUNT_ID, EMPTY));
      roomInfoRepository.findByCourseId(courseId)
          .subscribe(existingRoom -> roomController.inviteUser(existingRoom.getRoom_id(), userName)
              .subscribe(user -> logger.info("InviteUserResponse " + user)));
      logger.info("Event - invite user");

    }
  }

  /**
   * Consume update enrollment.
   *
   * @param eventModel the event model
   */
  @SuppressWarnings("unchecked")
  @RabbitListener(queues = {"${canvaslms.enrollment.update.event}"})
  public void consumeUpdateEnrollment(EventModel eventModel) {
    logger.info("Message receved from update enrollment: {}", eventModel);
    Document payload = eventModel.getEvent();
    Map<String, String> body = payload.get(BODY, Map.class);
    String userId = body.get(USER_ID);
    // calling canvas to get userName
    String userName = canvasUserService.getUserName(userId);
    if (userName != null) {
      Integer courseId = Integer.parseInt(body.get(COURSE_ID).replaceFirst(ACCOUNT_ID, EMPTY));
      String workFlowState = body.get(WORKFLOWSTATE);
      if (StringUtils.equalsAnyIgnoreCase(workFlowState, DELETED)
          || StringUtils.equalsAnyIgnoreCase(workFlowState, INACTIVE)) {
        roomInfoRepository.findByCourseId(courseId).subscribe(existingRoom -> roomController
            .removeUser(existingRoom.getRoom_id(), userName, REASON).subscribe());
        logger.info("Event - remove user");
      } else {
        roomInfoRepository.findByCourseId(courseId).subscribe(
            existingRoom -> roomController.inviteUser(existingRoom.getRoom_id(), userName)
                .subscribe(user -> logger.info("InviteUserResponse " + user)));
        logger.info("Update enrollment - Invite user");
      }
    }

  }
}
