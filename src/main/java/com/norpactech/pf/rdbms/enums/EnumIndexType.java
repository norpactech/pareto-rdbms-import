package com.norpactech.pf.rdbms.enums;

public enum EnumIndexType {
  
  PRIMARY_KEY("primary_key"), 
  ALTERNATE_KEY("alt_key");
  
  private String name;

  private EnumIndexType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static EnumIndexType fromName(String name) {
    for (EnumIndexType type : EnumIndexType.values()) {
      if (type.getName().equals(name)) {
        return type;
      }
    }
    return null;
  }
}