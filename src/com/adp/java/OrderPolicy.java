/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.adp.java;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum OrderPolicy implements org.apache.thrift.TEnum {
  MEDIA_BUYER(1),
  HERD_BUYER(2),
  BACKBONES_BUYER(3);

  private final int value;

  private OrderPolicy(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static OrderPolicy findByValue(int value) { 
    switch (value) {
      case 1:
        return MEDIA_BUYER;
      case 2:
        return HERD_BUYER;
      case 3:
        return BACKBONES_BUYER;
      default:
        return null;
    }
  }
}
