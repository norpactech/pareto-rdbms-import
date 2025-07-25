package com.norpactech.pf.rdbms.enums;

public enum EnumCommonProperty {
  
  ID("id"), 
  CREATED_AT("created_at"),
  CREATED_BY("created_by"), 
  UPDATED_AT("updated_at"),
  UPDATED_BY("updated_by"),
  IS_ACTIVE("is_active");
  
  private String name;

  private EnumCommonProperty(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static EnumCommonProperty fromName(String name) {
    for (EnumCommonProperty type : EnumCommonProperty.values()) {
      if (type.getName().equals(name)) {
        return type;
      }
    }
    return null;
  }
}