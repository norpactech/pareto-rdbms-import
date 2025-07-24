package com.norpactech.pf.rdbms.enums;

public enum EnumValiationType {

  ENUM("Enum"), 
  REGEXP("RegExp"),
  LENGTH("Length"), 
  NOTNULL("NotNull"),
  LOGIC("logic");
  
  private String name;

  private EnumValiationType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static EnumRefTableType fromName(String name) {
    for (EnumRefTableType type : EnumRefTableType.values()) {
      if (type.getName().equals(name)) {
        return type;
      }
    }
    return null;
  }
}
