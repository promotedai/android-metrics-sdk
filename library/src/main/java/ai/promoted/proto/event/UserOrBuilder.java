// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/event/event.proto

package ai.promoted.proto.event;

public interface UserOrBuilder extends
    // @@protoc_insertion_point(interface_extends:event.User)
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
   * Optional.  Custom properties per platform.
   * </pre>
   *
   * <code>.common.Properties properties = 6;</code>
   * @return Whether the properties field is set.
   */
  boolean hasProperties();
  /**
   * <pre>
   * Optional.  Custom properties per platform.
   * </pre>
   *
   * <code>.common.Properties properties = 6;</code>
   * @return The properties.
   */
  ai.promoted.proto.common.Properties getProperties();
  /**
   * <pre>
   * Optional.  Custom properties per platform.
   * </pre>
   *
   * <code>.common.Properties properties = 6;</code>
   */
  ai.promoted.proto.common.PropertiesOrBuilder getPropertiesOrBuilder();
}
