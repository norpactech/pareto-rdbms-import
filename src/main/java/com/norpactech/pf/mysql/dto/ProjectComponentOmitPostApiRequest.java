package com.norpactech.pf.mysql.dto;
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
 * Post API Request
 * 
 * M:M Project Component/Data Object
 */
 public class ProjectComponentOmitPostApiRequest {

  private UUID idProjectComponent;
  private UUID idDataObject;

  public ProjectComponentOmitPostApiRequest () {}

  @Hidden
  public Map<String, Object> getInsertRequest() {
    
    var request = new LinkedHashMap<String, Object>();
    request.put("sql", "SELECT pareto.i_project_component_omit(?,?)");
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