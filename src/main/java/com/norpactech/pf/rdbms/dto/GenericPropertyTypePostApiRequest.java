package com.norpactech.pf.rdbms.dto;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.Map;
import java.util.UUID;
import java.util.LinkedHashMap;
import io.swagger.v3.oas.annotations.Hidden;

/**
 * Post API Request
 * 
 * Generic Property Types
 */
 public class GenericPropertyTypePostApiRequest {

  private UUID idGenericDataType;
  private UUID idValidation;
  private String name;
  private String description;
  private Integer length;
  private Integer scale;
  private Boolean isNullable;
  private String defaultValue;
  private String createdBy;

  public GenericPropertyTypePostApiRequest () {}

  @Hidden
  public Map<String, Object> getInsertRequest() {
    
    var request = new LinkedHashMap<String, Object>();
    request.put("sql", "SELECT pareto.i_generic_property_type(?,?,?,?,?,?,?,?,?)");
    request.put("idGenericDataType", this.idGenericDataType);
    request.put("idValidation", this.idValidation);
    request.put("name", this.name);
    request.put("description", this.description);
    request.put("length", this.length);
    request.put("scale", this.scale);
    request.put("isNullable", this.isNullable);
    request.put("defaultValue", this.defaultValue);
    request.put("createdBy", this.createdBy);
    return request;
  }
  
  // Getters and Setters...
    
  public UUID getIdGenericDataType() {
    return this.idGenericDataType;
  }
    
  public UUID setIdGenericDataType(UUID idGenericDataType) {
    return this.idGenericDataType = idGenericDataType;
  }    
    
  public UUID getIdValidation() {
    return this.idValidation;
  }
    
  public UUID setIdValidation(UUID idValidation) {
    return this.idValidation = idValidation;
  }    
    
  public String getName() {
    return this.name;
  }
    
  public String setName(String name) {
    return this.name = name;
  }    
    
  public String getDescription() {
    return this.description;
  }
    
  public String setDescription(String description) {
    return this.description = description;
  }    
    
  public Integer getLength() {
    return this.length;
  }
    
  public Integer setLength(Integer length) {
    return this.length = length;
  }    
    
  public Integer getScale() {
    return this.scale;
  }
    
  public Integer setScale(Integer scale) {
    return this.scale = scale;
  }    
    
  public Boolean getIsNullable() {
    return this.isNullable;
  }
    
  public Boolean setIsNullable(Boolean isNullable) {
    return this.isNullable = isNullable;
  }    
    
  public String getDefaultValue() {
    return this.defaultValue;
  }
    
  public String setDefaultValue(String defaultValue) {
    return this.defaultValue = defaultValue;
  }    
    
  public String getCreatedBy() {
    return this.createdBy;
  }
    
  public String setCreatedBy(String createdBy) {
    return this.createdBy = createdBy;
  }    
}