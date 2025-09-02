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
import com.norpactech.pf.rdbms.dto.ContextDataTypeDeleteApiRequest;
import com.norpactech.pf.rdbms.dto.ContextDataTypePutApiRequest;
import com.norpactech.pf.rdbms.dto.ContextDataTypePostApiRequest;

import com.norpactech.pf.rdbms.model.ContextDataType;

public class ContextDataTypeRepository extends ParetoNativeRepository<ContextDataType> {
  
  private static final String RELATIVE_URL = "/context-data-type";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public ContextDataType get(UUID id) throws Exception {
    return super.findOne(ContextDataType.class, new HashMap<>(Map.of("id", id)));
  }

  public ContextDataType findOne(UUID idTenant, UUID idContext, UUID idGenericDataType) throws Exception {
    return super.findOne(ContextDataType.class, new HashMap<>(Map.of("idTenant", idTenant, "idContext", idContext, "idGenericDataType", idGenericDataType)));
  }
  
  public List<ContextDataType> find(Map<String, Object> params) throws Exception {
    return super.find(ContextDataType.class, params);
  }
    
  public ApiResponse save(ContextDataTypePostApiRequest request) throws Exception {
    return super.post(toParams(request));
  }  
  
  public ApiResponse save(ContextDataTypePutApiRequest request) throws Exception {
    return super.put(toParams(request));
  } 

  public ApiResponse delete(ContextDataTypeDeleteApiRequest request) throws Exception {
    return super.delete(toParams(request));
  }
}