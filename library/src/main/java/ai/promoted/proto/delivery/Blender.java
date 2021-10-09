// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/delivery/blender.proto

package ai.promoted.proto.delivery;

public final class Blender {
  private Blender() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_delivery_BlenderRule_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_delivery_BlenderRule_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_delivery_PositiveRule_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_delivery_PositiveRule_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_delivery_InsertRule_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_delivery_InsertRule_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_delivery_NegativeRule_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_delivery_NegativeRule_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_delivery_DiversityRule_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_delivery_DiversityRule_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_delivery_BlenderConfig_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_delivery_BlenderConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_delivery_QualityScoreConfig_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_delivery_QualityScoreConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_delivery_QualityScoreTerm_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_delivery_QualityScoreTerm_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_delivery_NormalDistribution_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_delivery_NormalDistribution_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\034proto/delivery/blender.proto\022\010delivery" +
      "\"\373\001\n\013BlenderRule\022\026\n\016attribute_name\030\001 \001(\t" +
      "\022/\n\rpositive_rule\030\006 \001(\0132\026.delivery.Posit" +
      "iveRuleH\000\022+\n\013insert_rule\030\007 \001(\0132\024.deliver" +
      "y.InsertRuleH\000\022/\n\rnegative_rule\030\010 \001(\0132\026." +
      "delivery.NegativeRuleH\000\0221\n\016diversity_rul" +
      "e\030\t \001(\0132\027.delivery.DiversityRuleH\000B\006\n\004ru" +
      "leJ\004\010\n\020\013J\004\010\002\020\006\"z\n\014PositiveRule\022\027\n\nselect" +
      "_pct\030\001 \001(\001H\000\210\001\001\022\024\n\007min_pos\030\002 \001(\004H\001\210\001\001\022\024\n" +
      "\007max_pos\030\003 \001(\004H\002\210\001\001B\r\n\013_select_pctB\n\n\010_m" +
      "in_posB\n\n\010_max_pos\"x\n\nInsertRule\022\027\n\nsele" +
      "ct_pct\030\001 \001(\001H\000\210\001\001\022\024\n\007min_pos\030\002 \001(\004H\001\210\001\001\022" +
      "\024\n\007max_pos\030\003 \001(\004H\002\210\001\001B\r\n\013_select_pctB\n\n\010" +
      "_min_posB\n\n\010_max_pos\"\356\001\n\014NegativeRule\022\026\n" +
      "\tpluck_pct\030\001 \001(\001H\000\210\001\001\022\034\n\017forbid_less_pos" +
      "\030\002 \001(\004H\001\210\001\001\022\030\n\013min_spacing\030\003 \001(\004H\002\210\001\001\022\037\n" +
      "\022forbid_greater_pos\030\004 \001(\004H\003\210\001\001\022\026\n\tmax_co" +
      "unt\030\005 \001(\004H\004\210\001\001B\014\n\n_pluck_pctB\022\n\020_forbid_" +
      "less_posB\016\n\014_min_spacingB\025\n\023_forbid_grea" +
      "ter_posB\014\n\n_max_count\"-\n\rDiversityRule\022\022" +
      "\n\005multi\030\001 \001(\001H\000\210\001\001B\010\n\006_multi\"x\n\rBlenderC" +
      "onfig\022+\n\014blender_rule\030\001 \003(\0132\025.delivery.B" +
      "lenderRule\022:\n\024quality_score_config\030\002 \001(\013" +
      "2\034.delivery.QualityScoreConfig\"K\n\022Qualit" +
      "yScoreConfig\0225\n\021weighted_sum_term\030\001 \003(\0132" +
      "\032.delivery.QualityScoreTerm\"\367\001\n\020QualityS" +
      "coreTerm\022\030\n\016attribute_name\030\001 \001(\tH\000\0225\n\rra" +
      "ndom_normal\030\002 \001(\0132\034.delivery.NormalDistr" +
      "ibutionH\000\022\016\n\004ones\030\003 \001(\010H\000\022\027\n\nfetch_high\030" +
      "\n \001(\001H\001\210\001\001\022\026\n\tfetch_low\030\013 \001(\001H\002\210\001\001\022\016\n\006we" +
      "ight\030\014 \001(\001\022\016\n\006offset\030\r \001(\001B\016\n\014fetch_meth" +
      "odB\r\n\013_fetch_highB\014\n\n_fetch_lowJ\004\010\004\020\n\"4\n" +
      "\022NormalDistribution\022\014\n\004mean\030\001 \001(\001\022\020\n\010var" +
      "iance\030\002 \001(\001B\'\n\032ai.promoted.proto.deliver" +
      "yB\007BlenderP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_delivery_BlenderRule_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_delivery_BlenderRule_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_delivery_BlenderRule_descriptor,
        new String[] { "AttributeName", "PositiveRule", "InsertRule", "NegativeRule", "DiversityRule", "Rule", });
    internal_static_delivery_PositiveRule_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_delivery_PositiveRule_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_delivery_PositiveRule_descriptor,
        new String[] { "SelectPct", "MinPos", "MaxPos", "SelectPct", "MinPos", "MaxPos", });
    internal_static_delivery_InsertRule_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_delivery_InsertRule_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_delivery_InsertRule_descriptor,
        new String[] { "SelectPct", "MinPos", "MaxPos", "SelectPct", "MinPos", "MaxPos", });
    internal_static_delivery_NegativeRule_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_delivery_NegativeRule_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_delivery_NegativeRule_descriptor,
        new String[] { "PluckPct", "ForbidLessPos", "MinSpacing", "ForbidGreaterPos", "MaxCount", "PluckPct", "ForbidLessPos", "MinSpacing", "ForbidGreaterPos", "MaxCount", });
    internal_static_delivery_DiversityRule_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_delivery_DiversityRule_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_delivery_DiversityRule_descriptor,
        new String[] { "Multi", "Multi", });
    internal_static_delivery_BlenderConfig_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_delivery_BlenderConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_delivery_BlenderConfig_descriptor,
        new String[] { "BlenderRule", "QualityScoreConfig", });
    internal_static_delivery_QualityScoreConfig_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_delivery_QualityScoreConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_delivery_QualityScoreConfig_descriptor,
        new String[] { "WeightedSumTerm", });
    internal_static_delivery_QualityScoreTerm_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_delivery_QualityScoreTerm_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_delivery_QualityScoreTerm_descriptor,
        new String[] { "AttributeName", "RandomNormal", "Ones", "FetchHigh", "FetchLow", "Weight", "Offset", "FetchMethod", "FetchHigh", "FetchLow", });
    internal_static_delivery_NormalDistribution_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_delivery_NormalDistribution_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_delivery_NormalDistribution_descriptor,
        new String[] { "Mean", "Variance", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
