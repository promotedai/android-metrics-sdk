// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/common/common.proto

package ai.promoted.proto.common;

public interface ClientInfoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:common.ClientInfo)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.common.ClientInfo.ClientType client_type = 1;</code>
   * @return The enum numeric value on the wire for clientType.
   */
  int getClientTypeValue();
  /**
   * <code>.common.ClientInfo.ClientType client_type = 1;</code>
   * @return The clientType.
   */
  ClientInfo.ClientType getClientType();

  /**
   * <code>.common.ClientInfo.TrafficType traffic_type = 2;</code>
   * @return The enum numeric value on the wire for trafficType.
   */
  int getTrafficTypeValue();
  /**
   * <code>.common.ClientInfo.TrafficType traffic_type = 2;</code>
   * @return The trafficType.
   */
  ClientInfo.TrafficType getTrafficType();
}
