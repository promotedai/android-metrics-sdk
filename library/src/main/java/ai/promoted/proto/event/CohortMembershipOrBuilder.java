// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/event/event.proto

package ai.promoted.proto.event;

public interface CohortMembershipOrBuilder extends
    // @@protoc_insertion_point(interface_extends:event.CohortMembership)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Optional.  If not set, set by API servers.
   * If not set, API server uses LogRequest.platform_id.
   * </pre>
   *
   * <code>uint64 platform_id = 1;</code>
   * @return The platformId.
   */
  long getPlatformId();

  /**
   * <pre>
   * Optional.  Must be set on LogRequest or here.
   * </pre>
   *
   * <code>.common.UserInfo user_info = 2;</code>
   * @return Whether the userInfo field is set.
   */
  boolean hasUserInfo();
  /**
   * <pre>
   * Optional.  Must be set on LogRequest or here.
   * </pre>
   *
   * <code>.common.UserInfo user_info = 2;</code>
   * @return The userInfo.
   */
  ai.promoted.proto.common.UserInfo getUserInfo();
  /**
   * <pre>
   * Optional.  Must be set on LogRequest or here.
   * </pre>
   *
   * <code>.common.UserInfo user_info = 2;</code>
   */
  ai.promoted.proto.common.UserInfoOrBuilder getUserInfoOrBuilder();

  /**
   * <pre>
   * Optional.  If not set, set by API servers.
   * If not set, API server uses LogRequest.timing.
   * </pre>
   *
   * <code>.common.Timing timing = 3;</code>
   * @return Whether the timing field is set.
   */
  boolean hasTiming();
  /**
   * <pre>
   * Optional.  If not set, set by API servers.
   * If not set, API server uses LogRequest.timing.
   * </pre>
   *
   * <code>.common.Timing timing = 3;</code>
   * @return The timing.
   */
  ai.promoted.proto.common.Timing getTiming();
  /**
   * <pre>
   * Optional.  If not set, set by API servers.
   * If not set, API server uses LogRequest.timing.
   * </pre>
   *
   * <code>.common.Timing timing = 3;</code>
   */
  ai.promoted.proto.common.TimingOrBuilder getTimingOrBuilder();

  /**
   * <pre>
   * Optional.  If not set, API server uses LogRequest.client_info.
   * </pre>
   *
   * <code>.common.ClientInfo client_info = 4;</code>
   * @return Whether the clientInfo field is set.
   */
  boolean hasClientInfo();
  /**
   * <pre>
   * Optional.  If not set, API server uses LogRequest.client_info.
   * </pre>
   *
   * <code>.common.ClientInfo client_info = 4;</code>
   * @return The clientInfo.
   */
  ai.promoted.proto.common.ClientInfo getClientInfo();
  /**
   * <pre>
   * Optional.  If not set, API server uses LogRequest.client_info.
   * </pre>
   *
   * <code>.common.ClientInfo client_info = 4;</code>
   */
  ai.promoted.proto.common.ClientInfoOrBuilder getClientInfoOrBuilder();

  /**
   * <pre>
   * Optional.  Primary key.
   * SDKs usually handles this automatically. For details, see
   * https://github.com/promotedai/schema#setting-primary-keys
   * </pre>
   *
   * <code>string membership_id = 6;</code>
   * @return The membershipId.
   */
  String getMembershipId();
  /**
   * <pre>
   * Optional.  Primary key.
   * SDKs usually handles this automatically. For details, see
   * https://github.com/promotedai/schema#setting-primary-keys
   * </pre>
   *
   * <code>string membership_id = 6;</code>
   * @return The bytes for membershipId.
   */
  com.google.protobuf.ByteString
      getMembershipIdBytes();

  /**
   * <pre>
   * Optional.  This field refers to the cohort (currently stored as an enum).
   * </pre>
   *
   * <code>string cohort_id = 8;</code>
   * @return The cohortId.
   */
  String getCohortId();
  /**
   * <pre>
   * Optional.  This field refers to the cohort (currently stored as an enum).
   * </pre>
   *
   * <code>string cohort_id = 8;</code>
   * @return The bytes for cohortId.
   */
  com.google.protobuf.ByteString
      getCohortIdBytes();

  /**
   * <pre>
   * Optional.
   * </pre>
   *
   * <code>.event.CohortArm arm = 9;</code>
   * @return The enum numeric value on the wire for arm.
   */
  int getArmValue();
  /**
   * <pre>
   * Optional.
   * </pre>
   *
   * <code>.event.CohortArm arm = 9;</code>
   * @return The arm.
   */
  CohortArm getArm();

  /**
   * <pre>
   * Optional.  Custom properties per platform.
   * </pre>
   *
   * <code>.common.Properties properties = 10;</code>
   * @return Whether the properties field is set.
   */
  boolean hasProperties();
  /**
   * <pre>
   * Optional.  Custom properties per platform.
   * </pre>
   *
   * <code>.common.Properties properties = 10;</code>
   * @return The properties.
   */
  ai.promoted.proto.common.Properties getProperties();
  /**
   * <pre>
   * Optional.  Custom properties per platform.
   * </pre>
   *
   * <code>.common.Properties properties = 10;</code>
   */
  ai.promoted.proto.common.PropertiesOrBuilder getPropertiesOrBuilder();
}
