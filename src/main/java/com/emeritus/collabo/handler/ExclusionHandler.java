package com.emeritus.collabo.handler;

import java.util.Arrays;
import java.util.Map;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The Class ExclusionHandler.
 */
@Component
public class ExclusionHandler {

  /** The event exclusions. */
  @Value("${canvaslms.event.exclusions:null}")
  private String[] eventExclusions;

  /** The event inclusions. */
  @Value("${canvaslms.event.inclusions:null}")
  private String[] eventInclusions;

  /** The context role exclusions. */
  @Value("${canvaslms.event.attrs.context.role.exclusions:null}")
  private String[] contextRoleExclusions;

  /** The asset type exclusions. */
  @Value("${canvaslms.event.body.asset.type.exclusions:null}")
  private String[] assetTypeExclusions;

  /**
   * Exclude event.
   *
   * @param event the event
   * @return true, if successful
   */
  public boolean excludeEvent(Document event) {
    return excludeEventName(event) || excludeContextRole(event) || excludeAssetType(event);
  }

  /**
   * Exclude event name.
   *
   * @param payload the payload
   * @return true, if successful
   */
  @SuppressWarnings("unchecked")
  private boolean excludeEventName(Document payload) {
    Map<String, String> attributes = payload.get("attributes", Map.class);
    if (attributes != null) {
      String eventName = attributes.get("event_name");
      return ((eventExclusions != null) && (eventName != null)
          && Arrays.asList(eventExclusions).contains(eventName)
          // exclude events which are not in inclusion list
          && !Arrays.asList(eventInclusions).contains(eventName));
    }
    return false;
  }

  /**
   * Exclude context role.
   *
   * @param payload the payload
   * @return true, if successful
   */
  @SuppressWarnings("unchecked")
  private boolean excludeContextRole(Document payload) {
    Map<String, String> attributes = payload.get("attributes", Map.class);
    if (attributes != null) {
      String contextRole = attributes.get("context_role");
      return ((contextRoleExclusions != null) && (contextRole != null)
          && Arrays.asList(contextRoleExclusions).contains(contextRole));

    }
    return false;
  }

  /**
   * Exclude asset type.
   *
   * @param payload the payload
   * @return true, if successful
   */
  @SuppressWarnings("unchecked")
  private boolean excludeAssetType(Document payload) {
    Map<String, String> attributes = payload.get("body", Map.class);
    if (attributes != null) {
      String assetType = attributes.get("asset_type");
      return ((assetTypeExclusions != null) && (assetType != null)
          && Arrays.asList(assetTypeExclusions).contains(assetType));
    }
    return false;
  }

}
