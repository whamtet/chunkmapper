// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: RailRegionContainer.proto

package com.chunkmapper.protoc;

public final class RailRegionContainer {
  private RailRegionContainer() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }
  public interface RailRegionOrBuilder
      extends com.google.protobuf.MessageLiteOrBuilder {

    // repeated .RailSection rail_sections = 1;
    /**
     * <code>repeated .RailSection rail_sections = 1;</code>
     */
    java.util.List<com.chunkmapper.protoc.RailSectionContainer.RailSection> 
        getRailSectionsList();
    /**
     * <code>repeated .RailSection rail_sections = 1;</code>
     */
    com.chunkmapper.protoc.RailSectionContainer.RailSection getRailSections(int index);
    /**
     * <code>repeated .RailSection rail_sections = 1;</code>
     */
    int getRailSectionsCount();
  }
  /**
   * Protobuf type {@code RailRegion}
   */
  public static final class RailRegion extends
      com.google.protobuf.GeneratedMessageLite
      implements RailRegionOrBuilder {
    // Use RailRegion.newBuilder() to construct.
    private RailRegion(com.google.protobuf.GeneratedMessageLite.Builder builder) {
      super(builder);

    }
    private RailRegion(boolean noInit) {}

    private static final RailRegion defaultInstance;
    public static RailRegion getDefaultInstance() {
      return defaultInstance;
    }

    public RailRegion getDefaultInstanceForType() {
      return defaultInstance;
    }

    private RailRegion(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                railSections_ = new java.util.ArrayList<com.chunkmapper.protoc.RailSectionContainer.RailSection>();
                mutable_bitField0_ |= 0x00000001;
              }
              railSections_.add(input.readMessage(com.chunkmapper.protoc.RailSectionContainer.RailSection.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          railSections_ = java.util.Collections.unmodifiableList(railSections_);
        }
        makeExtensionsImmutable();
      }
    }
    public static com.google.protobuf.Parser<RailRegion> PARSER =
        new com.google.protobuf.AbstractParser<RailRegion>() {
      public RailRegion parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new RailRegion(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<RailRegion> getParserForType() {
      return PARSER;
    }

    // repeated .RailSection rail_sections = 1;
    public static final int RAIL_SECTIONS_FIELD_NUMBER = 1;
    private java.util.List<com.chunkmapper.protoc.RailSectionContainer.RailSection> railSections_;
    /**
     * <code>repeated .RailSection rail_sections = 1;</code>
     */
    public java.util.List<com.chunkmapper.protoc.RailSectionContainer.RailSection> getRailSectionsList() {
      return railSections_;
    }
    /**
     * <code>repeated .RailSection rail_sections = 1;</code>
     */
    public java.util.List<? extends com.chunkmapper.protoc.RailSectionContainer.RailSectionOrBuilder> 
        getRailSectionsOrBuilderList() {
      return railSections_;
    }
    /**
     * <code>repeated .RailSection rail_sections = 1;</code>
     */
    public int getRailSectionsCount() {
      return railSections_.size();
    }
    /**
     * <code>repeated .RailSection rail_sections = 1;</code>
     */
    public com.chunkmapper.protoc.RailSectionContainer.RailSection getRailSections(int index) {
      return railSections_.get(index);
    }
    /**
     * <code>repeated .RailSection rail_sections = 1;</code>
     */
    public com.chunkmapper.protoc.RailSectionContainer.RailSectionOrBuilder getRailSectionsOrBuilder(
        int index) {
      return railSections_.get(index);
    }

    private void initFields() {
      railSections_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getRailSectionsCount(); i++) {
        if (!getRailSections(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      for (int i = 0; i < railSections_.size(); i++) {
        output.writeMessage(1, railSections_.get(i));
      }
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < railSections_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, railSections_.get(i));
      }
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.chunkmapper.protoc.RailRegionContainer.RailRegion parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.chunkmapper.protoc.RailRegionContainer.RailRegion parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.chunkmapper.protoc.RailRegionContainer.RailRegion parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.chunkmapper.protoc.RailRegionContainer.RailRegion parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.chunkmapper.protoc.RailRegionContainer.RailRegion parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.chunkmapper.protoc.RailRegionContainer.RailRegion parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.chunkmapper.protoc.RailRegionContainer.RailRegion parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.chunkmapper.protoc.RailRegionContainer.RailRegion parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.chunkmapper.protoc.RailRegionContainer.RailRegion parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.chunkmapper.protoc.RailRegionContainer.RailRegion parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.chunkmapper.protoc.RailRegionContainer.RailRegion prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    /**
     * Protobuf type {@code RailRegion}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.chunkmapper.protoc.RailRegionContainer.RailRegion, Builder>
        implements com.chunkmapper.protoc.RailRegionContainer.RailRegionOrBuilder {
      // Construct using com.chunkmapper.protoc.RailRegionContainer.RailRegion.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private void maybeForceBuilderInitialization() {
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        railSections_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.chunkmapper.protoc.RailRegionContainer.RailRegion getDefaultInstanceForType() {
        return com.chunkmapper.protoc.RailRegionContainer.RailRegion.getDefaultInstance();
      }

      public com.chunkmapper.protoc.RailRegionContainer.RailRegion build() {
        com.chunkmapper.protoc.RailRegionContainer.RailRegion result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.chunkmapper.protoc.RailRegionContainer.RailRegion buildPartial() {
        com.chunkmapper.protoc.RailRegionContainer.RailRegion result = new com.chunkmapper.protoc.RailRegionContainer.RailRegion(this);
        int from_bitField0_ = bitField0_;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          railSections_ = java.util.Collections.unmodifiableList(railSections_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.railSections_ = railSections_;
        return result;
      }

      public Builder mergeFrom(com.chunkmapper.protoc.RailRegionContainer.RailRegion other) {
        if (other == com.chunkmapper.protoc.RailRegionContainer.RailRegion.getDefaultInstance()) return this;
        if (!other.railSections_.isEmpty()) {
          if (railSections_.isEmpty()) {
            railSections_ = other.railSections_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureRailSectionsIsMutable();
            railSections_.addAll(other.railSections_);
          }
          
        }
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getRailSectionsCount(); i++) {
          if (!getRailSections(i).isInitialized()) {
            
            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.chunkmapper.protoc.RailRegionContainer.RailRegion parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.chunkmapper.protoc.RailRegionContainer.RailRegion) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // repeated .RailSection rail_sections = 1;
      private java.util.List<com.chunkmapper.protoc.RailSectionContainer.RailSection> railSections_ =
        java.util.Collections.emptyList();
      private void ensureRailSectionsIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          railSections_ = new java.util.ArrayList<com.chunkmapper.protoc.RailSectionContainer.RailSection>(railSections_);
          bitField0_ |= 0x00000001;
         }
      }

      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public java.util.List<com.chunkmapper.protoc.RailSectionContainer.RailSection> getRailSectionsList() {
        return java.util.Collections.unmodifiableList(railSections_);
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public int getRailSectionsCount() {
        return railSections_.size();
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public com.chunkmapper.protoc.RailSectionContainer.RailSection getRailSections(int index) {
        return railSections_.get(index);
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public Builder setRailSections(
          int index, com.chunkmapper.protoc.RailSectionContainer.RailSection value) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRailSectionsIsMutable();
        railSections_.set(index, value);

        return this;
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public Builder setRailSections(
          int index, com.chunkmapper.protoc.RailSectionContainer.RailSection.Builder builderForValue) {
        ensureRailSectionsIsMutable();
        railSections_.set(index, builderForValue.build());

        return this;
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public Builder addRailSections(com.chunkmapper.protoc.RailSectionContainer.RailSection value) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRailSectionsIsMutable();
        railSections_.add(value);

        return this;
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public Builder addRailSections(
          int index, com.chunkmapper.protoc.RailSectionContainer.RailSection value) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRailSectionsIsMutable();
        railSections_.add(index, value);

        return this;
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public Builder addRailSections(
          com.chunkmapper.protoc.RailSectionContainer.RailSection.Builder builderForValue) {
        ensureRailSectionsIsMutable();
        railSections_.add(builderForValue.build());

        return this;
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public Builder addRailSections(
          int index, com.chunkmapper.protoc.RailSectionContainer.RailSection.Builder builderForValue) {
        ensureRailSectionsIsMutable();
        railSections_.add(index, builderForValue.build());

        return this;
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public Builder addAllRailSections(
          java.lang.Iterable<? extends com.chunkmapper.protoc.RailSectionContainer.RailSection> values) {
        ensureRailSectionsIsMutable();
        super.addAll(values, railSections_);

        return this;
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public Builder clearRailSections() {
        railSections_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);

        return this;
      }
      /**
       * <code>repeated .RailSection rail_sections = 1;</code>
       */
      public Builder removeRailSections(int index) {
        ensureRailSectionsIsMutable();
        railSections_.remove(index);

        return this;
      }

      // @@protoc_insertion_point(builder_scope:RailRegion)
    }

    static {
      defaultInstance = new RailRegion(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:RailRegion)
  }


  static {
  }

  // @@protoc_insertion_point(outer_class_scope)
}