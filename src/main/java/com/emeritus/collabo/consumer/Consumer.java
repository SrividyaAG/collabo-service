package com.emeritus.collabo.consumer;

import java.util.Map;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.emeritus.collabo.controller.RoomController;
import com.emeritus.collabo.model.EventModel;
import com.emeritus.collabo.repository.RoomInfoRepository;

/**
 * The Class Consumer.
 */
@Component
public class Consumer {

  /** The Constant NAME. */
  private static final String NAME = "name";

  /** The Constant USER_LOGIN. */
  private static final String USER_LOGIN = "user_login";

  /** The Constant EMPTY. */
  private static final String EMPTY = "";

  /** The Constant COURSE_ID. */
  private static final String COURSE_ID = "course_id";

  /** The Constant BODY. */
  private static final String BODY = "body";

  /** The Constant ATTRIBUTES. */
  private static final String ATTRIBUTES = "attributes";

  /** The Constant ACCOUNT_ID. */
  private static final String ACCOUNT_ID = "1";

  /** The logger. */
  private Logger logger = LoggerFactory.getLogger(Consumer.class);

  /** The room controller. */
  @Autowired
  private RoomController roomController;

  /** The room info repository. */
  @Autowired
  private RoomInfoRepository roomInfoRepository;

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
    roomController.createRoom(courseName, courseId).subscribe(room -> logger.info("RoomResponse " + room));
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
    Map<String, String> attributes = payload.get(ATTRIBUTES, Map.class);
    String userName = attributes.get(USER_LOGIN);
    Map<String, String> body = payload.get(BODY, Map.class);
    Integer courseId = Integer.parseInt(body.get(COURSE_ID).replaceFirst(ACCOUNT_ID, EMPTY));
    roomInfoRepository.findByCourseId(courseId)
        .subscribe(existingRoom -> roomController.inviteUser(existingRoom.getRoom_id(), userName)
            .subscribe(user -> logger.info("InviteUserResponse " + user)));
    logger.info("Event - invite user");
  }
}
