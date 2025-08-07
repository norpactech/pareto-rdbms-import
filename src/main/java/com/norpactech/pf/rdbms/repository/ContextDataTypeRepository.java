package com.norpactech.pf.rdbms.repository;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.norpactech.pf.rdbms.dto.ContextDataTypePostApiRequest;
import com.norpactech.pf.rdbms.dto.ContextDataTypePutApiRequest;
import com.norpactech.pf.rdbms.model.ContextDataType;
import com.norpactech.pf.rdbms.utils.ApiResponse;

public class ContextDataTypeRepository extends ParetoApiRepository<ContextDataType> {

  private static final String RELATIVE_URL = "/context-data-type";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public ContextDataType findOne(UUID idContext, String name) throws Exception {
    return findOne(ContextDataType.class, new HashMap<>(Map.of("idContext", idContext.toString(), "contextValue", name)));
  } 
  
  public List<ContextDataType> findAll(UUID idContext) throws Exception {
    return find(ContextDataType.class, new HashMap<>(Map.of("idContext", idContext.toString())));
  }   
  
  public void save(ContextDataTypePostApiRequest request) throws Exception {
    
    ApiResponse response = post(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Context Data TYpe '" + request.getName() + "' was not saved! Terminating...");
    }
  }  
  
  public void save(ContextDataTypePutApiRequest request) throws Exception {
    
    ApiResponse response = put(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Context Data Type '" + request.getName() + "' was not saved! Terminating...");
    }
  }
}