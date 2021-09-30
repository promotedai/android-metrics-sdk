// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/delivery/delivery.proto

package ai.promoted.proto.delivery;

/**
 * <pre>
 * Used to indicate the client's use case.  Used on both View and Request.
 * Next ID = 12.
 * </pre>
 *
 * Protobuf enum {@code delivery.UseCase}
 */
public enum UseCase
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>UNKNOWN_USE_CASE = 0;</code>
   */
  UNKNOWN_USE_CASE(0),
  /**
   * <pre>
   * Need to handle in wrapper proto.
   * </pre>
   *
   * <code>CUSTOM = 1;</code>
   */
  CUSTOM(1),
  /**
   * <code>SEARCH = 2;</code>
   */
  SEARCH(2),
  /**
   * <code>SEARCH_SUGGESTIONS = 3;</code>
   */
  SEARCH_SUGGESTIONS(3),
  /**
   * <code>FEED = 4;</code>
   */
  FEED(4),
  /**
   * <code>RELATED_CONTENT = 5;</code>
   */
  RELATED_CONTENT(5),
  /**
   * <code>CLOSE_UP = 6;</code>
   */
  CLOSE_UP(6),
  /**
   * <code>CATEGORY_CONTENT = 7;</code>
   */
  CATEGORY_CONTENT(7),
  /**
   * <code>MY_CONTENT = 8;</code>
   */
  MY_CONTENT(8),
  /**
   * <code>MY_SAVED_CONTENT = 9;</code>
   */
  MY_SAVED_CONTENT(9),
  /**
   * <code>SELLER_CONTENT = 10;</code>
   */
  SELLER_CONTENT(10),
  /**
   * <code>DISCOVER = 11;</code>
   */
  DISCOVER(11),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>UNKNOWN_USE_CASE = 0;</code>
   */
  public static final int UNKNOWN_USE_CASE_VALUE = 0;
  /**
   * <pre>
   * Need to handle in wrapper proto.
   * </pre>
   *
   * <code>CUSTOM = 1;</code>
   */
  public static final int CUSTOM_VALUE = 1;
  /**
   * <code>SEARCH = 2;</code>
   */
  public static final int SEARCH_VALUE = 2;
  /**
   * <code>SEARCH_SUGGESTIONS = 3;</code>
   */
  public static final int SEARCH_SUGGESTIONS_VALUE = 3;
  /**
   * <code>FEED = 4;</code>
   */
  public static final int FEED_VALUE = 4;
  /**
   * <code>RELATED_CONTENT = 5;</code>
   */
  public static final int RELATED_CONTENT_VALUE = 5;
  /**
   * <code>CLOSE_UP = 6;</code>
   */
  public static final int CLOSE_UP_VALUE = 6;
  /**
   * <code>CATEGORY_CONTENT = 7;</code>
   */
  public static final int CATEGORY_CONTENT_VALUE = 7;
  /**
   * <code>MY_CONTENT = 8;</code>
   */
  public static final int MY_CONTENT_VALUE = 8;
  /**
   * <code>MY_SAVED_CONTENT = 9;</code>
   */
  public static final int MY_SAVED_CONTENT_VALUE = 9;
  /**
   * <code>SELLER_CONTENT = 10;</code>
   */
  public static final int SELLER_CONTENT_VALUE = 10;
  /**
   * <code>DISCOVER = 11;</code>
   */
  public static final int DISCOVER_VALUE = 11;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @Deprecated
  public static UseCase valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static UseCase forNumber(int value) {
    switch (value) {
      case 0: return UNKNOWN_USE_CASE;
      case 1: return CUSTOM;
      case 2: return SEARCH;
      case 3: return SEARCH_SUGGESTIONS;
      case 4: return FEED;
      case 5: return RELATED_CONTENT;
      case 6: return CLOSE_UP;
      case 7: return CATEGORY_CONTENT;
      case 8: return MY_CONTENT;
      case 9: return MY_SAVED_CONTENT;
      case 10: return SELLER_CONTENT;
      case 11: return DISCOVER;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<UseCase>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      UseCase> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<UseCase>() {
          public UseCase findValueByNumber(int number) {
            return UseCase.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return Delivery.getDescriptor().getEnumTypes().get(0);
  }

  private static final UseCase[] VALUES = values();

  public static UseCase valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private UseCase(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:delivery.UseCase)
}

