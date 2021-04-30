// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/event/event.proto

package ai.promoted.proto.event;

public interface ClientHintsOrBuilder extends
    // @@protoc_insertion_point(interface_extends:event.ClientHints)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>bool is_mobile = 1;</code>
   * @return The isMobile.
   */
  boolean getIsMobile();

  /**
   * <code>repeated .event.ClientHintBrand brand = 2;</code>
   */
  java.util.List<ai.promoted.proto.event.ClientHintBrand> 
      getBrandList();
  /**
   * <code>repeated .event.ClientHintBrand brand = 2;</code>
   */
  ai.promoted.proto.event.ClientHintBrand getBrand(int index);
  /**
   * <code>repeated .event.ClientHintBrand brand = 2;</code>
   */
  int getBrandCount();
  /**
   * <code>repeated .event.ClientHintBrand brand = 2;</code>
   */
  java.util.List<? extends ai.promoted.proto.event.ClientHintBrandOrBuilder> 
      getBrandOrBuilderList();
  /**
   * <code>repeated .event.ClientHintBrand brand = 2;</code>
   */
  ai.promoted.proto.event.ClientHintBrandOrBuilder getBrandOrBuilder(
      int index);

  /**
   * <code>string architecture = 3;</code>
   * @return The architecture.
   */
  java.lang.String getArchitecture();
  /**
   * <code>string architecture = 3;</code>
   * @return The bytes for architecture.
   */
  com.google.protobuf.ByteString
      getArchitectureBytes();

  /**
   * <code>string model = 4;</code>
   * @return The model.
   */
  java.lang.String getModel();
  /**
   * <code>string model = 4;</code>
   * @return The bytes for model.
   */
  com.google.protobuf.ByteString
      getModelBytes();

  /**
   * <code>string platform = 5;</code>
   * @return The platform.
   */
  java.lang.String getPlatform();
  /**
   * <code>string platform = 5;</code>
   * @return The bytes for platform.
   */
  com.google.protobuf.ByteString
      getPlatformBytes();

  /**
   * <code>string platform_version = 6;</code>
   * @return The platformVersion.
   */
  java.lang.String getPlatformVersion();
  /**
   * <code>string platform_version = 6;</code>
   * @return The bytes for platformVersion.
   */
  com.google.protobuf.ByteString
      getPlatformVersionBytes();

  /**
   * <code>string ua_full_version = 7;</code>
   * @return The uaFullVersion.
   */
  java.lang.String getUaFullVersion();
  /**
   * <code>string ua_full_version = 7;</code>
   * @return The bytes for uaFullVersion.
   */
  com.google.protobuf.ByteString
      getUaFullVersionBytes();
}