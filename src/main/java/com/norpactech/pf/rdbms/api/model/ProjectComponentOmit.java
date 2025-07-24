package com.norpactech.pf.rdbms.api.model;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.lang.String;
import java.util.Map;
import java.util.UUID;

/**
 * API Model Class: ProjectComponentOmit - M:M Project Component/Data Object
 */
public class ProjectComponentOmit extends BaseModel {

  private UUID idProjectComponent;
  private String projectComponentName;
  private UUID idDataObject;
  private String dataObjectName;

  public ProjectComponentOmit () {}
  public ProjectComponentOmit (Object obj) {
    super(obj);
  }

  public ProjectComponentOmit (
    UUID idProjectComponent,
    String projectComponentName,
    UUID idDataObject,
    String dataObjectName)
 {
    this.idProjectComponent = idProjectComponent;
    this.projectComponentName = projectComponentName;
    this.idDataObject = idDataObject;
    this.dataObjectName = dataObjectName;
  }

  public static Map<String, Object> queryRequest(Map<String, String> queryParams) throws Exception {
    
    var matchedParams = paramMatcher(queryParams, ProjectComponentOmit.class);
    matchedParams.put("sql", 
      "SELECT pareto.project_component_omit.*, " + 
      "pareto.project_component.name as project_component_name, " + 
      "pareto.data_object.name as data_object_name " + 
      "FROM pareto.project_component_omit " + 
      "JOIN pareto.project_component on (pareto.project_component.id = pareto.project_component_omit.id_project_component) " + 
      "JOIN pareto.data_object on (pareto.data_object.id = pareto.project_component_omit.id_data_object)");
    return matchedParams;
  }

  // Getters and Setters...
    
  public UUID getIdProjectComponent() {
    return this.idProjectComponent;
  }
    
  public UUID setIdProjectComponent(UUID idProjectComponent) {
    return this.idProjectComponent = idProjectComponent;
  }    
    
  public String getProjectComponentName() {
    return this.projectComponentName;
  }
    
  public String setProjectComponentName(String projectComponentName) {
    return this.projectComponentName = projectComponentName;
  }    
    
  public UUID getIdDataObject() {
    return this.idDataObject;
  }
    
  public UUID setIdDataObject(UUID idDataObject) {
    return this.idDataObject = idDataObject;
  }    
    
  public String getDataObjectName() {
    return this.dataObjectName;
  }
    
  public String setDataObjectName(String dataObjectName) {
    return this.dataObjectName = dataObjectName;
  }    
}