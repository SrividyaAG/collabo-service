package com.emeritus.collabo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.UserReader;
import edu.ksu.canvas.model.User;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;

/**
 * The Class CanvasUserService.
 */
@Service
public class CanvasUserService {

  /** The Constant AT_ENCODED. */
  private static final String AT_ENCODED = "=40";

  /** The Constant AT. */
  private static final String AT = "@";

  /** The logger. */
  private Logger logger = LoggerFactory.getLogger(CanvasUserService.class);

  /** The admin token. */
  @Value("${canvas.admin-token}")
  private String adminToken;

  /** The base url. */
  @Value("${canvas.baseurl}")
  private String baseUrl;

  /** The base url. */
  @Value("${matrix.suffix}")
  private String matrixSuffix;

  /**
   * Gets the user by user id.
   *
   * @param userId the user id
   * @return the user by user id
   */
  public String getUserName(String userId) {
    String userName = null;
    try {
      NonRefreshableOauthToken oauthToken = new NonRefreshableOauthToken(adminToken);
      CanvasApiFactory canvasApiFactory = new CanvasApiFactory(baseUrl);
      UserReader userReader = canvasApiFactory.getReader(UserReader.class, oauthToken);
      User user = userReader.showUserDetails(userId).get();
      if (user != null) {
        // @srividya=40gmail.com:matrix.emeritus.org
        userName = user.getLoginId().replaceAll(AT, AT_ENCODED) + matrixSuffix;
        return userName;
      }
    } catch (Exception e) {
      logger.error("Error occurred while fetching users: {}", e.getMessage(), e);
    }
    return userName;
  }
}
