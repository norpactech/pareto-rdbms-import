package com.norpactech.pf.rdbms.repository;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import com.norpactech.pf.utils.ApiResponse;
import com.norpactech.pf.rdbms.dto.DataObjectPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataObjectPutApiRequest;
import com.norpactech.pf.rdbms.dto.DataObjectDeleteApiRequest;

import com.norpactech.pf.rdbms.model.DataObject;

public class DataObjectRepository extends ParetoNativeRepository<DataObject> {
  
  private static final String RELATIVE_URL = "/data-object";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public DataObject get(UUID id) throws Exception {
    return super.findOne(DataObject.class, new HashMap<>(Map.of("id", id)));
  }

  public DataObject findOne(UUID idSchema, String name) throws Exception {
    return super.findOne(DataObject.class, new HashMap<>(Map.of("idSchema", idSchema, "name", name)));
  }
  
  public List<DataObject> find(Map<String, Object> params) throws Exception {
    return super.find(DataObject.class, params);
  }
    
  public ApiResponse save(DataObjectPostApiRequest request) throws Exception {
    return super.post(toParams(request));
  }  
  
  public ApiResponse save(DataObjectPutApiRequest request) throws Exception {
    return super.put(toParams(request));
  } 

  public ApiResponse delete(DataObjectDeleteApiRequest request) throws Exception {
    return super.delete(toParams(request));
  }
}