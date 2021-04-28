// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/event/event.proto

package ai.promoted.proto.event;

public interface ScreenOrBuilder extends
    // @@protoc_insertion_point(interface_extends:event.Screen)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Android: DisplayMetrics.widthPixels/heightPixels
   * iOS: UIScreen.nativeBounds.width/height
   * </pre>
   *
   * <code>.event.Size size = 1;</code>
   * @return Whether the size field is set.
   */
  boolean hasSize();
  /**
   * <pre>
   * Android: DisplayMetrics.widthPixels/heightPixels
   * iOS: UIScreen.nativeBounds.width/height
   * </pre>
   *
   * <code>.event.Size size = 1;</code>
   * @return The size.
   */
  ai.promoted.proto.event.Size getSize();
  /**
   * <pre>
   * Android: DisplayMetrics.widthPixels/heightPixels
   * iOS: UIScreen.nativeBounds.width/height
   * </pre>
   *
   * <code>.event.Size size = 1;</code>
   */
  ai.promoted.proto.event.SizeOrBuilder getSizeOrBuilder();

  /**
   * <pre>
   * Natural scale factor.
   * Android: DisplayMetrics.density
   * iOS: UIScreen.scale
   * </pre>
   *
   * <code>float scale = 2;</code>
   * @return The scale.
   */
  float getScale();
}
