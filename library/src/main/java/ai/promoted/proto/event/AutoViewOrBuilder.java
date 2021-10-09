// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/event/event.proto

package ai.promoted.proto.event;

public interface AutoViewOrBuilder extends
    // @@protoc_insertion_point(interface_extends:event.AutoView)
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
   * <code>string auto_view_id = 6;</code>
   * @return The autoViewId.
   */
  String getAutoViewId();
  /**
   * <pre>
   * Optional.  Primary key.
   * SDKs usually handles this automatically. For details, see
   * https://github.com/promotedai/schema#setting-primary-keys
   * </pre>
   *
   * <code>string auto_view_id = 6;</code>
   * @return The bytes for autoViewId.
   */
  com.google.protobuf.ByteString
      getAutoViewIdBytes();

  /**
   * <pre>
   * Optional.
   * </pre>
   *
   * <code>string session_id = 7;</code>
   * @return The sessionId.
   */
  String getSessionId();
  /**
   * <pre>
   * Optional.
   * </pre>
   *
   * <code>string session_id = 7;</code>
   * @return The bytes for sessionId.
   */
  com.google.protobuf.ByteString
      getSessionIdBytes();

  /**
   * <pre>
   * Optional.  The name of the view.
   * </pre>
   *
   * <code>string name = 8;</code>
   * @return The name.
   */
  String getName();
  /**
   * <pre>
   * Optional.  The name of the view.
   * </pre>
   *
   * <code>string name = 8;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <pre>
   * Optional.
   * </pre>
   *
   * <code>.delivery.UseCase use_case = 9;</code>
   * @return The enum numeric value on the wire for useCase.
   */
  int getUseCaseValue();
  /**
   * <pre>
   * Optional.
   * </pre>
   *
   * <code>.delivery.UseCase use_case = 9;</code>
   * @return The useCase.
   */
  ai.promoted.proto.delivery.UseCase getUseCase();

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

  /**
   * <pre>
   * Optional.
   * </pre>
   *
   * <code>.common.Locale locale = 11;</code>
   * @return Whether the locale field is set.
   */
  boolean hasLocale();
  /**
   * <pre>
   * Optional.
   * </pre>
   *
   * <code>.common.Locale locale = 11;</code>
   * @return The locale.
   */
  ai.promoted.proto.common.Locale getLocale();
  /**
   * <pre>
   * Optional.
   * </pre>
   *
   * <code>.common.Locale locale = 11;</code>
   */
  ai.promoted.proto.common.LocaleOrBuilder getLocaleOrBuilder();

  /**
   * <code>.event.WebPageView web_page_view = 12;</code>
   * @return Whether the webPageView field is set.
   */
  boolean hasWebPageView();
  /**
   * <code>.event.WebPageView web_page_view = 12;</code>
   * @return The webPageView.
   */
  WebPageView getWebPageView();
  /**
   * <code>.event.WebPageView web_page_view = 12;</code>
   */
  WebPageViewOrBuilder getWebPageViewOrBuilder();

  /**
   * <code>.event.AppScreenView app_screen_view = 13;</code>
   * @return Whether the appScreenView field is set.
   */
  boolean hasAppScreenView();
  /**
   * <code>.event.AppScreenView app_screen_view = 13;</code>
   * @return The appScreenView.
   */
  AppScreenView getAppScreenView();
  /**
   * <code>.event.AppScreenView app_screen_view = 13;</code>
   */
  AppScreenViewOrBuilder getAppScreenViewOrBuilder();

  public AutoView.UiTypeCase getUiTypeCase();
}
