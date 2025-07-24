package com.norpactech.pf.rdbms.api.model;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.lang.Boolean;
import java.lang.String;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

/**
 * API Model Class: DataObject - Object
 */
public class DataObject extends BaseModel {

  private UUID id;
  private UUID idSchema;
  private String schemaName;
  private String name;
  private String description;
  private Boolean hasIdentifier;
  private Boolean hasAudit;
  private Boolean hasActive;
  private Timestamp createdAt;
  private String createdBy;
  private Timestamp updatedAt;
  private String updatedBy;
  private Boolean isActive;

  public DataObject () {}
  public DataObject (Object obj) {
    super(obj);
  }

  public DataObject (
    UUID id,
    UUID idSchema,
    String schemaName,
    String name,
    String description,
    Boolean hasIdentifier,
    Boolean hasAudit,
    Boolean hasActive,
    Timestamp createdAt,
    String createdBy,
    Timestamp updatedAt,
    String updatedBy,
    Boolean isActive)
 {
    this.id = id;
    this.idSchema = idSchema;
    this.schemaName = schemaName;
    this.name = name;
    this.description = description;
    this.hasIdentifier = hasIdentifier;
    this.hasAudit = hasAudit;
    this.hasActive = hasActive;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.updatedAt = updatedAt;
    this.updatedBy = updatedBy;
    this.isActive = isActive;
  }

  public static Map<String, Object> queryRequest(Map<String, String> queryParams) throws Exception {
    
    var matchedParams = paramMatcher(queryParams, DataObject.class);
    matchedParams.put("sql", 
      "SELECT pareto.data_object.*, " + 
      "pareto.schema.name as schema_name " + 
      "FROM pareto.data_object " + 
      "JOIN pareto.schema on (pareto.schema.id = pareto.data_object.id_schema)");
    return matchedParams;
  }

  // Getters and Setters...
    
  public UUID getId() {
    return this.id;
  }
    
  public UUID setId(UUID id) {
    return this.id = id;
  }    
    
  public UUID getIdSchema() {
    return this.idSchema;
  }
    
  public UUID setIdSchema(UUID idSchema) {
    return this.idSchema = idSchema;
  }    
    
  public String getSchemaName() {
    return this.schemaName;
  }
    
  public String setSchemaName(String schemaName) {
    return this.schemaName = schemaName;
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
    
  public Boolean getHasIdentifier() {
    return this.hasIdentifier;
  }
    
  public Boolean setHasIdentifier(Boolean hasIdentifier) {
    return this.hasIdentifier = hasIdentifier;
  }    
    
  public Boolean getHasAudit() {
    return this.hasAudit;
  }
    
  public Boolean setHasAudit(Boolean hasAudit) {
    return this.hasAudit = hasAudit;
  }    
    
  public Boolean getHasActive() {
    return this.hasActive;
  }
    
  public Boolean setHasActive(Boolean hasActive) {
    return this.hasActive = hasActive;
  }    
    
  public Timestamp getCreatedAt() {
    return this.createdAt;
  }
    
  public Timestamp setCreatedAt(Timestamp createdAt) {
    return this.createdAt = createdAt;
  }    
    
  public String getCreatedBy() {
    return this.createdBy;
  }
    
  public String setCreatedBy(String createdBy) {
    return this.createdBy = createdBy;
  }    
    
  public Timestamp getUpdatedAt() {
    return this.updatedAt;
  }
    
  public Timestamp setUpdatedAt(Timestamp updatedAt) {
    return this.updatedAt = updatedAt;
  }    
    
  public String getUpdatedBy() {
    return this.updatedBy;
  }
    
  public String setUpdatedBy(String updatedBy) {
    return this.updatedBy = updatedBy;
  }    
    
  public Boolean getIsActive() {
    return this.isActive;
  }
    
  public Boolean setIsActive(Boolean isActive) {
    return this.isActive = isActive;
  }    
}