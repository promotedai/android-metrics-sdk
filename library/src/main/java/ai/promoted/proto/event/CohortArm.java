// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/event/event.proto

package ai.promoted.proto.event;

/**
 * <pre>
 * The arm (experiment's group) when the Cohort is for an experiment.
 * Next ID = 6.
 * </pre>
 *
 * Protobuf enum {@code event.CohortArm}
 */
public enum CohortArm
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>UNKNOWN_GROUP = 0;</code>
   */
  UNKNOWN_GROUP(0),
  /**
   * <code>CONTROL = 1;</code>
   */
  CONTROL(1),
  /**
   * <code>TREATMENT = 2;</code>
   */
  TREATMENT(2),
  /**
   * <pre>
   * These are generic arms (groups) that can be used when there are multiple treatments.
   * </pre>
   *
   * <code>TREATMENT1 = 3;</code>
   */
  TREATMENT1(3),
  /**
   * <code>TREATMENT2 = 4;</code>
   */
  TREATMENT2(4),
  /**
   * <code>TREATMENT3 = 5;</code>
   */
  TREATMENT3(5),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>UNKNOWN_GROUP = 0;</code>
   */
  public static final int UNKNOWN_GROUP_VALUE = 0;
  /**
   * <code>CONTROL = 1;</code>
   */
  public static final int CONTROL_VALUE = 1;
  /**
   * <code>TREATMENT = 2;</code>
   */
  public static final int TREATMENT_VALUE = 2;
  /**
   * <pre>
   * These are generic arms (groups) that can be used when there are multiple treatments.
   * </pre>
   *
   * <code>TREATMENT1 = 3;</code>
   */
  public static final int TREATMENT1_VALUE = 3;
  /**
   * <code>TREATMENT2 = 4;</code>
   */
  public static final int TREATMENT2_VALUE = 4;
  /**
   * <code>TREATMENT3 = 5;</code>
   */
  public static final int TREATMENT3_VALUE = 5;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static CohortArm valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static CohortArm forNumber(int value) {
    switch (value) {
      case 0: return UNKNOWN_GROUP;
      case 1: return CONTROL;
      case 2: return TREATMENT;
      case 3: return TREATMENT1;
      case 4: return TREATMENT2;
      case 5: return TREATMENT3;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<CohortArm>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      CohortArm> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<CohortArm>() {
          public CohortArm findValueByNumber(int number) {
            return CohortArm.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalStateException(
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
    return ai.promoted.proto.event.Event.getDescriptor().getEnumTypes().get(0);
  }

  private static final CohortArm[] VALUES = values();

  public static CohortArm valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private CohortArm(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:event.CohortArm)
}

