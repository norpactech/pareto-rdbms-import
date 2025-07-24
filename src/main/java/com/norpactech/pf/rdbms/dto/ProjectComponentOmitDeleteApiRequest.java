package com.norpactech.pf.rdbms.dto;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.util.Map;
import java.util.UUID;
import java.util.LinkedHashMap;
import io.swagger.v3.oas.annotations.Hidden;

/**
 * Delete API Request
 * 
 * M:M Project Component/Data Object
 */
public class ProjectComponentOmitDeleteApiRequest {

  private UUID idProjectComponent;
  private UUID idDataObject;

  public ProjectComponentOmitDeleteApiRequest () {}

  @Hidden
  public Map<String, Object> getDeleteRequest() {
    
    var request = new LinkedHashMap<String, Object>();
    request.put("sql", "SELECT pareto.d_project_component_omit(?,?)");
    request.put("idProjectComponent", this.idProjectComponent);
    request.put("idDataObject", this.idDataObject);
    return request;
  }
  
  // Getters and Setters...
    
  public UUID getIdProjectComponent() {
    return this.idProjectComponent;
  }
    
  public UUID setIdProjectComponent(UUID idProjectComponent) {
    return this.idProjectComponent = idProjectComponent;
  }    
    
  public UUID getIdDataObject() {
    return this.idDataObject;
  }
    
  public UUID setIdDataObject(UUID idDataObject) {
    return this.idDataObject = idDataObject;
  }    
}