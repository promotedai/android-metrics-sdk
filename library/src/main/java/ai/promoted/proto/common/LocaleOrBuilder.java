// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/common/common.proto

package ai.promoted.proto.common;

public interface LocaleOrBuilder extends
    // @@protoc_insertion_point(interface_extends:common.Locale)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * CodeReview - Which ISO code is this?  ISO 639-1? 2? 3?
   * "en", "zh_Hant", "fr"
   * </pre>
   *
   * <code>string language_code = 1;</code>
   * @return The languageCode.
   */
  String getLanguageCode();
  /**
   * <pre>
   * CodeReview - Which ISO code is this?  ISO 639-1? 2? 3?
   * "en", "zh_Hant", "fr"
   * </pre>
   *
   * <code>string language_code = 1;</code>
   * @return The bytes for languageCode.
   */
  com.google.protobuf.ByteString
      getLanguageCodeBytes();

  /**
   * <pre>
   * CodeReview - Which ISO code?  ISO 3166-1?
   * "US", "CA", "FR"
   * </pre>
   *
   * <code>string region_code = 2;</code>
   * @return The regionCode.
   */
  String getRegionCode();
  /**
   * <pre>
   * CodeReview - Which ISO code?  ISO 3166-1?
   * "US", "CA", "FR"
   * </pre>
   *
   * <code>string region_code = 2;</code>
   * @return The bytes for regionCode.
   */
  com.google.protobuf.ByteString
      getRegionCodeBytes();
}