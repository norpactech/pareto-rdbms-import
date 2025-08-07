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

import com.norpactech.pf.rdbms.dto.DataObjectPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataObjectPutApiRequest;
import com.norpactech.pf.rdbms.model.DataObject;
import com.norpactech.pf.rdbms.utils.ApiResponse;

public class DataObjectRepository extends ParetoApiRepository<DataObject> {

  private static final String RELATIVE_URL = "/data-object";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public DataObject findOne(UUID idSchema, String name) throws Exception {
    return findOne(DataObject.class, new HashMap<>(Map.of("idSchema", idSchema.toString(), "name", name)));
  } 
  
  public List<DataObject> findAll(UUID idSchema) throws Exception {
    return find(DataObject.class, new HashMap<>(Map.of("idSchema", idSchema.toString())));
  }   
  
  public void save(DataObjectPostApiRequest request) throws Exception {
    
    ApiResponse response = post(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Data Object '" + request.getName() + "' was not saved! Terminating...");
    }
  }  
  
  public void save(DataObjectPutApiRequest request) throws Exception {
    
    ApiResponse response = put(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Data Object '" + request.getName() + "' was not saved! Terminating...");
    }
  }    
}