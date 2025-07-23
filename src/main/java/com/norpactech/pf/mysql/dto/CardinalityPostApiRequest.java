package com.norpactech.pf.mysql.dto;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.lang.Boolean;
import java.lang.String;
import java.util.Map;
import java.util.UUID;
import java.util.LinkedHashMap;
import io.swagger.v3.oas.annotations.Hidden;

/**
 * Post API Request
 * 
 * Object Cardinality
 */
 public class CardinalityPostApiRequest {

  private UUID idProperty;
  private UUID idObjectReference;
  private UUID idRtCardinality;
  private UUID idRtCardinalityStrength;
  private Boolean hasReferencialAction;
  private String createdBy;

  public CardinalityPostApiRequest () {}

  @Hidden
  public Map<String, Object> getInsertRequest() {
    
    var request = new LinkedHashMap<String, Object>();
    request.put("sql", "SELECT pareto.i_cardinality(?,?,?,?,?,?)");
    request.put("idProperty", this.idProperty);
    request.put("idObjectReference", this.idObjectReference);
    request.put("idRtCardinality", this.idRtCardinality);
    request.put("idRtCardinalityStrength", this.idRtCardinalityStrength);
    request.put("hasReferencialAction", this.hasReferencialAction);
    request.put("createdBy", this.createdBy);
    return request;
  }
  
  // Getters and Setters...
    
  public UUID getIdProperty() {
    return this.idProperty;
  }
    
  public UUID setIdProperty(UUID idProperty) {
    return this.idProperty = idProperty;
  }    
    
  public UUID getIdObjectReference() {
    return this.idObjectReference;
  }
    
  public UUID setIdObjectReference(UUID idObjectReference) {
    return this.idObjectReference = idObjectReference;
  }    
    
  public UUID getIdRtCardinality() {
    return this.idRtCardinality;
  }
    
  public UUID setIdRtCardinality(UUID idRtCardinality) {
    return this.idRtCardinality = idRtCardinality;
  }    
    
  public UUID getIdRtCardinalityStrength() {
    return this.idRtCardinalityStrength;
  }
    
  public UUID setIdRtCardinalityStrength(UUID idRtCardinalityStrength) {
    return this.idRtCardinalityStrength = idRtCardinalityStrength;
  }    
    
  public Boolean getHasReferencialAction() {
    return this.hasReferencialAction;
  }
    
  public Boolean setHasReferencialAction(Boolean hasReferencialAction) {
    return this.hasReferencialAction = hasReferencialAction;
  }    
    
  public String getCreatedBy() {
    return this.createdBy;
  }
    
  public String setCreatedBy(String createdBy) {
    return this.createdBy = createdBy;
  }    
}