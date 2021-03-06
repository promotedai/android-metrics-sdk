// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/event/event.proto

package ai.promoted.proto.event;

/**
 * <pre>
 * History of errors from client.
 * Next ID = 3.
 * </pre>
 *
 * Protobuf type {@code event.ErrorHistory}
 */
public final class ErrorHistory extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:event.ErrorHistory)
    ErrorHistoryOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ErrorHistory.newBuilder() to construct.
  private ErrorHistory(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ErrorHistory() {
    iosErrors_ = java.util.Collections.emptyList();
  }

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(
      UnusedPrivateParameter unused) {
    return new ErrorHistory();
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ErrorHistory(
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
              iosErrors_ = new java.util.ArrayList<IOSError>();
              mutable_bitField0_ |= 0x00000001;
            }
            iosErrors_.add(
                input.readMessage(IOSError.parser(), extensionRegistry));
            break;
          }
          case 16: {

            totalErrors_ = input.readInt32();
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
        iosErrors_ = java.util.Collections.unmodifiableList(iosErrors_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return Event.internal_static_event_ErrorHistory_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return Event.internal_static_event_ErrorHistory_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            ErrorHistory.class, Builder.class);
  }

  public static final int IOS_ERRORS_FIELD_NUMBER = 1;
  private java.util.List<IOSError> iosErrors_;
  /**
   * <pre>
   * Window of latest errors.
   * </pre>
   *
   * <code>repeated .event.IOSError ios_errors = 1;</code>
   */
  @Override
  public java.util.List<IOSError> getIosErrorsList() {
    return iosErrors_;
  }
  /**
   * <pre>
   * Window of latest errors.
   * </pre>
   *
   * <code>repeated .event.IOSError ios_errors = 1;</code>
   */
  @Override
  public java.util.List<? extends IOSErrorOrBuilder>
      getIosErrorsOrBuilderList() {
    return iosErrors_;
  }
  /**
   * <pre>
   * Window of latest errors.
   * </pre>
   *
   * <code>repeated .event.IOSError ios_errors = 1;</code>
   */
  @Override
  public int getIosErrorsCount() {
    return iosErrors_.size();
  }
  /**
   * <pre>
   * Window of latest errors.
   * </pre>
   *
   * <code>repeated .event.IOSError ios_errors = 1;</code>
   */
  @Override
  public IOSError getIosErrors(int index) {
    return iosErrors_.get(index);
  }
  /**
   * <pre>
   * Window of latest errors.
   * </pre>
   *
   * <code>repeated .event.IOSError ios_errors = 1;</code>
   */
  @Override
  public IOSErrorOrBuilder getIosErrorsOrBuilder(
      int index) {
    return iosErrors_.get(index);
  }

  public static final int TOTAL_ERRORS_FIELD_NUMBER = 2;
  private int totalErrors_;
  /**
   * <pre>
   * Total number of errors encountered.
   * </pre>
   *
   * <code>int32 total_errors = 2;</code>
   * @return The totalErrors.
   */
  @Override
  public int getTotalErrors() {
    return totalErrors_;
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
    for (int i = 0; i < iosErrors_.size(); i++) {
      output.writeMessage(1, iosErrors_.get(i));
    }
    if (totalErrors_ != 0) {
      output.writeInt32(2, totalErrors_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < iosErrors_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, iosErrors_.get(i));
    }
    if (totalErrors_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, totalErrors_);
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
    if (!(obj instanceof ErrorHistory)) {
      return super.equals(obj);
    }
    ErrorHistory other = (ErrorHistory) obj;

    if (!getIosErrorsList()
        .equals(other.getIosErrorsList())) return false;
    if (getTotalErrors()
        != other.getTotalErrors()) return false;
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
    if (getIosErrorsCount() > 0) {
      hash = (37 * hash) + IOS_ERRORS_FIELD_NUMBER;
      hash = (53 * hash) + getIosErrorsList().hashCode();
    }
    hash = (37 * hash) + TOTAL_ERRORS_FIELD_NUMBER;
    hash = (53 * hash) + getTotalErrors();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static ErrorHistory parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static ErrorHistory parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static ErrorHistory parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static ErrorHistory parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static ErrorHistory parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static ErrorHistory parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static ErrorHistory parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static ErrorHistory parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static ErrorHistory parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static ErrorHistory parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static ErrorHistory parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static ErrorHistory parseFrom(
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
  public static Builder newBuilder(ErrorHistory prototype) {
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
   * History of errors from client.
   * Next ID = 3.
   * </pre>
   *
   * Protobuf type {@code event.ErrorHistory}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:event.ErrorHistory)
      ErrorHistoryOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return Event.internal_static_event_ErrorHistory_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return Event.internal_static_event_ErrorHistory_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ErrorHistory.class, Builder.class);
    }

    // Construct using ai.promoted.proto.event.ErrorHistory.newBuilder()
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
        getIosErrorsFieldBuilder();
      }
    }
    @Override
    public Builder clear() {
      super.clear();
      if (iosErrorsBuilder_ == null) {
        iosErrors_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        iosErrorsBuilder_.clear();
      }
      totalErrors_ = 0;

      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return Event.internal_static_event_ErrorHistory_descriptor;
    }

    @Override
    public ErrorHistory getDefaultInstanceForType() {
      return ErrorHistory.getDefaultInstance();
    }

    @Override
    public ErrorHistory build() {
      ErrorHistory result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public ErrorHistory buildPartial() {
      ErrorHistory result = new ErrorHistory(this);
      int from_bitField0_ = bitField0_;
      if (iosErrorsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          iosErrors_ = java.util.Collections.unmodifiableList(iosErrors_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.iosErrors_ = iosErrors_;
      } else {
        result.iosErrors_ = iosErrorsBuilder_.build();
      }
      result.totalErrors_ = totalErrors_;
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
      if (other instanceof ErrorHistory) {
        return mergeFrom((ErrorHistory)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(ErrorHistory other) {
      if (other == ErrorHistory.getDefaultInstance()) return this;
      if (iosErrorsBuilder_ == null) {
        if (!other.iosErrors_.isEmpty()) {
          if (iosErrors_.isEmpty()) {
            iosErrors_ = other.iosErrors_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureIosErrorsIsMutable();
            iosErrors_.addAll(other.iosErrors_);
          }
          onChanged();
        }
      } else {
        if (!other.iosErrors_.isEmpty()) {
          if (iosErrorsBuilder_.isEmpty()) {
            iosErrorsBuilder_.dispose();
            iosErrorsBuilder_ = null;
            iosErrors_ = other.iosErrors_;
            bitField0_ = (bitField0_ & ~0x00000001);
            iosErrorsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getIosErrorsFieldBuilder() : null;
          } else {
            iosErrorsBuilder_.addAllMessages(other.iosErrors_);
          }
        }
      }
      if (other.getTotalErrors() != 0) {
        setTotalErrors(other.getTotalErrors());
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
      ErrorHistory parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (ErrorHistory) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<IOSError> iosErrors_ =
      java.util.Collections.emptyList();
    private void ensureIosErrorsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        iosErrors_ = new java.util.ArrayList<IOSError>(iosErrors_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        IOSError, IOSError.Builder, IOSErrorOrBuilder> iosErrorsBuilder_;

    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public java.util.List<IOSError> getIosErrorsList() {
      if (iosErrorsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(iosErrors_);
      } else {
        return iosErrorsBuilder_.getMessageList();
      }
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public int getIosErrorsCount() {
      if (iosErrorsBuilder_ == null) {
        return iosErrors_.size();
      } else {
        return iosErrorsBuilder_.getCount();
      }
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public IOSError getIosErrors(int index) {
      if (iosErrorsBuilder_ == null) {
        return iosErrors_.get(index);
      } else {
        return iosErrorsBuilder_.getMessage(index);
      }
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public Builder setIosErrors(
        int index, IOSError value) {
      if (iosErrorsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureIosErrorsIsMutable();
        iosErrors_.set(index, value);
        onChanged();
      } else {
        iosErrorsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public Builder setIosErrors(
        int index, IOSError.Builder builderForValue) {
      if (iosErrorsBuilder_ == null) {
        ensureIosErrorsIsMutable();
        iosErrors_.set(index, builderForValue.build());
        onChanged();
      } else {
        iosErrorsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public Builder addIosErrors(IOSError value) {
      if (iosErrorsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureIosErrorsIsMutable();
        iosErrors_.add(value);
        onChanged();
      } else {
        iosErrorsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public Builder addIosErrors(
        int index, IOSError value) {
      if (iosErrorsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureIosErrorsIsMutable();
        iosErrors_.add(index, value);
        onChanged();
      } else {
        iosErrorsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public Builder addIosErrors(
        IOSError.Builder builderForValue) {
      if (iosErrorsBuilder_ == null) {
        ensureIosErrorsIsMutable();
        iosErrors_.add(builderForValue.build());
        onChanged();
      } else {
        iosErrorsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public Builder addIosErrors(
        int index, IOSError.Builder builderForValue) {
      if (iosErrorsBuilder_ == null) {
        ensureIosErrorsIsMutable();
        iosErrors_.add(index, builderForValue.build());
        onChanged();
      } else {
        iosErrorsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public Builder addAllIosErrors(
        Iterable<? extends IOSError> values) {
      if (iosErrorsBuilder_ == null) {
        ensureIosErrorsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, iosErrors_);
        onChanged();
      } else {
        iosErrorsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public Builder clearIosErrors() {
      if (iosErrorsBuilder_ == null) {
        iosErrors_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        iosErrorsBuilder_.clear();
      }
      return this;
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public Builder removeIosErrors(int index) {
      if (iosErrorsBuilder_ == null) {
        ensureIosErrorsIsMutable();
        iosErrors_.remove(index);
        onChanged();
      } else {
        iosErrorsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public IOSError.Builder getIosErrorsBuilder(
        int index) {
      return getIosErrorsFieldBuilder().getBuilder(index);
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public IOSErrorOrBuilder getIosErrorsOrBuilder(
        int index) {
      if (iosErrorsBuilder_ == null) {
        return iosErrors_.get(index);  } else {
        return iosErrorsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public java.util.List<? extends IOSErrorOrBuilder>
         getIosErrorsOrBuilderList() {
      if (iosErrorsBuilder_ != null) {
        return iosErrorsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(iosErrors_);
      }
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public IOSError.Builder addIosErrorsBuilder() {
      return getIosErrorsFieldBuilder().addBuilder(
          IOSError.getDefaultInstance());
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public IOSError.Builder addIosErrorsBuilder(
        int index) {
      return getIosErrorsFieldBuilder().addBuilder(
          index, IOSError.getDefaultInstance());
    }
    /**
     * <pre>
     * Window of latest errors.
     * </pre>
     *
     * <code>repeated .event.IOSError ios_errors = 1;</code>
     */
    public java.util.List<IOSError.Builder>
         getIosErrorsBuilderList() {
      return getIosErrorsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        IOSError, IOSError.Builder, IOSErrorOrBuilder>
        getIosErrorsFieldBuilder() {
      if (iosErrorsBuilder_ == null) {
        iosErrorsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            IOSError, IOSError.Builder, IOSErrorOrBuilder>(
                iosErrors_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        iosErrors_ = null;
      }
      return iosErrorsBuilder_;
    }

    private int totalErrors_ ;
    /**
     * <pre>
     * Total number of errors encountered.
     * </pre>
     *
     * <code>int32 total_errors = 2;</code>
     * @return The totalErrors.
     */
    @Override
    public int getTotalErrors() {
      return totalErrors_;
    }
    /**
     * <pre>
     * Total number of errors encountered.
     * </pre>
     *
     * <code>int32 total_errors = 2;</code>
     * @param value The totalErrors to set.
     * @return This builder for chaining.
     */
    public Builder setTotalErrors(int value) {
      
      totalErrors_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Total number of errors encountered.
     * </pre>
     *
     * <code>int32 total_errors = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearTotalErrors() {
      
      totalErrors_ = 0;
      onChanged();
      return this;
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


    // @@protoc_insertion_point(builder_scope:event.ErrorHistory)
  }

  // @@protoc_insertion_point(class_scope:event.ErrorHistory)
  private static final ErrorHistory DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new ErrorHistory();
  }

  public static ErrorHistory getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ErrorHistory>
      PARSER = new com.google.protobuf.AbstractParser<ErrorHistory>() {
    @Override
    public ErrorHistory parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ErrorHistory(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ErrorHistory> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<ErrorHistory> getParserForType() {
    return PARSER;
  }

  @Override
  public ErrorHistory getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

