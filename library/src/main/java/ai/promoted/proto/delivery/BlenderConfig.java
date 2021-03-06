// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/delivery/blender.proto

package ai.promoted.proto.delivery;

/**
 * <pre>
 * Next ID = 3.
 * </pre>
 *
 * Protobuf type {@code delivery.BlenderConfig}
 */
public final class BlenderConfig extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:delivery.BlenderConfig)
    BlenderConfigOrBuilder {
private static final long serialVersionUID = 0L;
  // Use BlenderConfig.newBuilder() to construct.
  private BlenderConfig(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private BlenderConfig() {
    blenderRule_ = java.util.Collections.emptyList();
  }

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(
      UnusedPrivateParameter unused) {
    return new BlenderConfig();
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private BlenderConfig(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              blenderRule_ = new java.util.ArrayList<BlenderRule>();
              mutable_bitField0_ |= 0x00000001;
            }
            blenderRule_.add(
                input.readMessage(BlenderRule.parser(), extensionRegistry));
            break;
          }
          case 18: {
            QualityScoreConfig.Builder subBuilder = null;
            if (qualityScoreConfig_ != null) {
              subBuilder = qualityScoreConfig_.toBuilder();
            }
            qualityScoreConfig_ = input.readMessage(QualityScoreConfig.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(qualityScoreConfig_);
              qualityScoreConfig_ = subBuilder.buildPartial();
            }

            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        blenderRule_ = java.util.Collections.unmodifiableList(blenderRule_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return Blender.internal_static_delivery_BlenderConfig_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return Blender.internal_static_delivery_BlenderConfig_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            BlenderConfig.class, Builder.class);
  }

  public static final int BLENDER_RULE_FIELD_NUMBER = 1;
  private java.util.List<BlenderRule> blenderRule_;
  /**
   * <pre>
   * List of blender rules.
   * </pre>
   *
   * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
   */
  @Override
  public java.util.List<BlenderRule> getBlenderRuleList() {
    return blenderRule_;
  }
  /**
   * <pre>
   * List of blender rules.
   * </pre>
   *
   * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
   */
  @Override
  public java.util.List<? extends BlenderRuleOrBuilder>
      getBlenderRuleOrBuilderList() {
    return blenderRule_;
  }
  /**
   * <pre>
   * List of blender rules.
   * </pre>
   *
   * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
   */
  @Override
  public int getBlenderRuleCount() {
    return blenderRule_.size();
  }
  /**
   * <pre>
   * List of blender rules.
   * </pre>
   *
   * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
   */
  @Override
  public BlenderRule getBlenderRule(int index) {
    return blenderRule_.get(index);
  }
  /**
   * <pre>
   * List of blender rules.
   * </pre>
   *
   * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
   */
  @Override
  public BlenderRuleOrBuilder getBlenderRuleOrBuilder(
      int index) {
    return blenderRule_.get(index);
  }

  public static final int QUALITY_SCORE_CONFIG_FIELD_NUMBER = 2;
  private QualityScoreConfig qualityScoreConfig_;
  /**
   * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
   * @return Whether the qualityScoreConfig field is set.
   */
  @Override
  public boolean hasQualityScoreConfig() {
    return qualityScoreConfig_ != null;
  }
  /**
   * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
   * @return The qualityScoreConfig.
   */
  @Override
  public QualityScoreConfig getQualityScoreConfig() {
    return qualityScoreConfig_ == null ? QualityScoreConfig.getDefaultInstance() : qualityScoreConfig_;
  }
  /**
   * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
   */
  @Override
  public QualityScoreConfigOrBuilder getQualityScoreConfigOrBuilder() {
    return getQualityScoreConfig();
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < blenderRule_.size(); i++) {
      output.writeMessage(1, blenderRule_.get(i));
    }
    if (qualityScoreConfig_ != null) {
      output.writeMessage(2, getQualityScoreConfig());
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < blenderRule_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, blenderRule_.get(i));
    }
    if (qualityScoreConfig_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getQualityScoreConfig());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof BlenderConfig)) {
      return super.equals(obj);
    }
    BlenderConfig other = (BlenderConfig) obj;

    if (!getBlenderRuleList()
        .equals(other.getBlenderRuleList())) return false;
    if (hasQualityScoreConfig() != other.hasQualityScoreConfig()) return false;
    if (hasQualityScoreConfig()) {
      if (!getQualityScoreConfig()
          .equals(other.getQualityScoreConfig())) return false;
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getBlenderRuleCount() > 0) {
      hash = (37 * hash) + BLENDER_RULE_FIELD_NUMBER;
      hash = (53 * hash) + getBlenderRuleList().hashCode();
    }
    if (hasQualityScoreConfig()) {
      hash = (37 * hash) + QUALITY_SCORE_CONFIG_FIELD_NUMBER;
      hash = (53 * hash) + getQualityScoreConfig().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static BlenderConfig parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static BlenderConfig parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static BlenderConfig parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static BlenderConfig parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static BlenderConfig parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static BlenderConfig parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static BlenderConfig parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static BlenderConfig parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static BlenderConfig parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static BlenderConfig parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static BlenderConfig parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static BlenderConfig parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(BlenderConfig prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * Next ID = 3.
   * </pre>
   *
   * Protobuf type {@code delivery.BlenderConfig}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:delivery.BlenderConfig)
      BlenderConfigOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return Blender.internal_static_delivery_BlenderConfig_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return Blender.internal_static_delivery_BlenderConfig_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              BlenderConfig.class, Builder.class);
    }

    // Construct using ai.promoted.proto.delivery.BlenderConfig.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getBlenderRuleFieldBuilder();
      }
    }
    @Override
    public Builder clear() {
      super.clear();
      if (blenderRuleBuilder_ == null) {
        blenderRule_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        blenderRuleBuilder_.clear();
      }
      if (qualityScoreConfigBuilder_ == null) {
        qualityScoreConfig_ = null;
      } else {
        qualityScoreConfig_ = null;
        qualityScoreConfigBuilder_ = null;
      }
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return Blender.internal_static_delivery_BlenderConfig_descriptor;
    }

    @Override
    public BlenderConfig getDefaultInstanceForType() {
      return BlenderConfig.getDefaultInstance();
    }

    @Override
    public BlenderConfig build() {
      BlenderConfig result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public BlenderConfig buildPartial() {
      BlenderConfig result = new BlenderConfig(this);
      int from_bitField0_ = bitField0_;
      if (blenderRuleBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          blenderRule_ = java.util.Collections.unmodifiableList(blenderRule_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.blenderRule_ = blenderRule_;
      } else {
        result.blenderRule_ = blenderRuleBuilder_.build();
      }
      if (qualityScoreConfigBuilder_ == null) {
        result.qualityScoreConfig_ = qualityScoreConfig_;
      } else {
        result.qualityScoreConfig_ = qualityScoreConfigBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }
    @Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.setField(field, value);
    }
    @Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.addRepeatedField(field, value);
    }
    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof BlenderConfig) {
        return mergeFrom((BlenderConfig)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(BlenderConfig other) {
      if (other == BlenderConfig.getDefaultInstance()) return this;
      if (blenderRuleBuilder_ == null) {
        if (!other.blenderRule_.isEmpty()) {
          if (blenderRule_.isEmpty()) {
            blenderRule_ = other.blenderRule_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureBlenderRuleIsMutable();
            blenderRule_.addAll(other.blenderRule_);
          }
          onChanged();
        }
      } else {
        if (!other.blenderRule_.isEmpty()) {
          if (blenderRuleBuilder_.isEmpty()) {
            blenderRuleBuilder_.dispose();
            blenderRuleBuilder_ = null;
            blenderRule_ = other.blenderRule_;
            bitField0_ = (bitField0_ & ~0x00000001);
            blenderRuleBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getBlenderRuleFieldBuilder() : null;
          } else {
            blenderRuleBuilder_.addAllMessages(other.blenderRule_);
          }
        }
      }
      if (other.hasQualityScoreConfig()) {
        mergeQualityScoreConfig(other.getQualityScoreConfig());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      BlenderConfig parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (BlenderConfig) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<BlenderRule> blenderRule_ =
      java.util.Collections.emptyList();
    private void ensureBlenderRuleIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        blenderRule_ = new java.util.ArrayList<BlenderRule>(blenderRule_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        BlenderRule, BlenderRule.Builder, BlenderRuleOrBuilder> blenderRuleBuilder_;

    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public java.util.List<BlenderRule> getBlenderRuleList() {
      if (blenderRuleBuilder_ == null) {
        return java.util.Collections.unmodifiableList(blenderRule_);
      } else {
        return blenderRuleBuilder_.getMessageList();
      }
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public int getBlenderRuleCount() {
      if (blenderRuleBuilder_ == null) {
        return blenderRule_.size();
      } else {
        return blenderRuleBuilder_.getCount();
      }
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public BlenderRule getBlenderRule(int index) {
      if (blenderRuleBuilder_ == null) {
        return blenderRule_.get(index);
      } else {
        return blenderRuleBuilder_.getMessage(index);
      }
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public Builder setBlenderRule(
        int index, BlenderRule value) {
      if (blenderRuleBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureBlenderRuleIsMutable();
        blenderRule_.set(index, value);
        onChanged();
      } else {
        blenderRuleBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public Builder setBlenderRule(
        int index, BlenderRule.Builder builderForValue) {
      if (blenderRuleBuilder_ == null) {
        ensureBlenderRuleIsMutable();
        blenderRule_.set(index, builderForValue.build());
        onChanged();
      } else {
        blenderRuleBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public Builder addBlenderRule(BlenderRule value) {
      if (blenderRuleBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureBlenderRuleIsMutable();
        blenderRule_.add(value);
        onChanged();
      } else {
        blenderRuleBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public Builder addBlenderRule(
        int index, BlenderRule value) {
      if (blenderRuleBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureBlenderRuleIsMutable();
        blenderRule_.add(index, value);
        onChanged();
      } else {
        blenderRuleBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public Builder addBlenderRule(
        BlenderRule.Builder builderForValue) {
      if (blenderRuleBuilder_ == null) {
        ensureBlenderRuleIsMutable();
        blenderRule_.add(builderForValue.build());
        onChanged();
      } else {
        blenderRuleBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public Builder addBlenderRule(
        int index, BlenderRule.Builder builderForValue) {
      if (blenderRuleBuilder_ == null) {
        ensureBlenderRuleIsMutable();
        blenderRule_.add(index, builderForValue.build());
        onChanged();
      } else {
        blenderRuleBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public Builder addAllBlenderRule(
        Iterable<? extends BlenderRule> values) {
      if (blenderRuleBuilder_ == null) {
        ensureBlenderRuleIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, blenderRule_);
        onChanged();
      } else {
        blenderRuleBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public Builder clearBlenderRule() {
      if (blenderRuleBuilder_ == null) {
        blenderRule_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        blenderRuleBuilder_.clear();
      }
      return this;
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public Builder removeBlenderRule(int index) {
      if (blenderRuleBuilder_ == null) {
        ensureBlenderRuleIsMutable();
        blenderRule_.remove(index);
        onChanged();
      } else {
        blenderRuleBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public BlenderRule.Builder getBlenderRuleBuilder(
        int index) {
      return getBlenderRuleFieldBuilder().getBuilder(index);
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public BlenderRuleOrBuilder getBlenderRuleOrBuilder(
        int index) {
      if (blenderRuleBuilder_ == null) {
        return blenderRule_.get(index);  } else {
        return blenderRuleBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public java.util.List<? extends BlenderRuleOrBuilder>
         getBlenderRuleOrBuilderList() {
      if (blenderRuleBuilder_ != null) {
        return blenderRuleBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(blenderRule_);
      }
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public BlenderRule.Builder addBlenderRuleBuilder() {
      return getBlenderRuleFieldBuilder().addBuilder(
          BlenderRule.getDefaultInstance());
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public BlenderRule.Builder addBlenderRuleBuilder(
        int index) {
      return getBlenderRuleFieldBuilder().addBuilder(
          index, BlenderRule.getDefaultInstance());
    }
    /**
     * <pre>
     * List of blender rules.
     * </pre>
     *
     * <code>repeated .delivery.BlenderRule blender_rule = 1;</code>
     */
    public java.util.List<BlenderRule.Builder>
         getBlenderRuleBuilderList() {
      return getBlenderRuleFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        BlenderRule, BlenderRule.Builder, BlenderRuleOrBuilder>
        getBlenderRuleFieldBuilder() {
      if (blenderRuleBuilder_ == null) {
        blenderRuleBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            BlenderRule, BlenderRule.Builder, BlenderRuleOrBuilder>(
                blenderRule_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        blenderRule_ = null;
      }
      return blenderRuleBuilder_;
    }

    private QualityScoreConfig qualityScoreConfig_;
    private com.google.protobuf.SingleFieldBuilderV3<
        QualityScoreConfig, QualityScoreConfig.Builder, QualityScoreConfigOrBuilder> qualityScoreConfigBuilder_;
    /**
     * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
     * @return Whether the qualityScoreConfig field is set.
     */
    public boolean hasQualityScoreConfig() {
      return qualityScoreConfigBuilder_ != null || qualityScoreConfig_ != null;
    }
    /**
     * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
     * @return The qualityScoreConfig.
     */
    public QualityScoreConfig getQualityScoreConfig() {
      if (qualityScoreConfigBuilder_ == null) {
        return qualityScoreConfig_ == null ? QualityScoreConfig.getDefaultInstance() : qualityScoreConfig_;
      } else {
        return qualityScoreConfigBuilder_.getMessage();
      }
    }
    /**
     * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
     */
    public Builder setQualityScoreConfig(QualityScoreConfig value) {
      if (qualityScoreConfigBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        qualityScoreConfig_ = value;
        onChanged();
      } else {
        qualityScoreConfigBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
     */
    public Builder setQualityScoreConfig(
        QualityScoreConfig.Builder builderForValue) {
      if (qualityScoreConfigBuilder_ == null) {
        qualityScoreConfig_ = builderForValue.build();
        onChanged();
      } else {
        qualityScoreConfigBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
     */
    public Builder mergeQualityScoreConfig(QualityScoreConfig value) {
      if (qualityScoreConfigBuilder_ == null) {
        if (qualityScoreConfig_ != null) {
          qualityScoreConfig_ =
            QualityScoreConfig.newBuilder(qualityScoreConfig_).mergeFrom(value).buildPartial();
        } else {
          qualityScoreConfig_ = value;
        }
        onChanged();
      } else {
        qualityScoreConfigBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
     */
    public Builder clearQualityScoreConfig() {
      if (qualityScoreConfigBuilder_ == null) {
        qualityScoreConfig_ = null;
        onChanged();
      } else {
        qualityScoreConfig_ = null;
        qualityScoreConfigBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
     */
    public QualityScoreConfig.Builder getQualityScoreConfigBuilder() {
      
      onChanged();
      return getQualityScoreConfigFieldBuilder().getBuilder();
    }
    /**
     * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
     */
    public QualityScoreConfigOrBuilder getQualityScoreConfigOrBuilder() {
      if (qualityScoreConfigBuilder_ != null) {
        return qualityScoreConfigBuilder_.getMessageOrBuilder();
      } else {
        return qualityScoreConfig_ == null ?
            QualityScoreConfig.getDefaultInstance() : qualityScoreConfig_;
      }
    }
    /**
     * <code>.delivery.QualityScoreConfig quality_score_config = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        QualityScoreConfig, QualityScoreConfig.Builder, QualityScoreConfigOrBuilder>
        getQualityScoreConfigFieldBuilder() {
      if (qualityScoreConfigBuilder_ == null) {
        qualityScoreConfigBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            QualityScoreConfig, QualityScoreConfig.Builder, QualityScoreConfigOrBuilder>(
                getQualityScoreConfig(),
                getParentForChildren(),
                isClean());
        qualityScoreConfig_ = null;
      }
      return qualityScoreConfigBuilder_;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:delivery.BlenderConfig)
  }

  // @@protoc_insertion_point(class_scope:delivery.BlenderConfig)
  private static final BlenderConfig DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new BlenderConfig();
  }

  public static BlenderConfig getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BlenderConfig>
      PARSER = new com.google.protobuf.AbstractParser<BlenderConfig>() {
    @Override
    public BlenderConfig parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new BlenderConfig(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<BlenderConfig> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<BlenderConfig> getParserForType() {
    return PARSER;
  }

  @Override
  public BlenderConfig getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

