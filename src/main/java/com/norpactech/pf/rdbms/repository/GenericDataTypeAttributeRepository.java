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
import com.norpactech.pf.rdbms.dto.GenericDataTypeAttributePutApiRequest;
import com.norpactech.pf.rdbms.dto.GenericDataTypeAttributePostApiRequest;
import com.norpactech.pf.rdbms.dto.GenericDataTypeAttributeDeleteApiRequest;

import com.norpactech.pf.rdbms.model.GenericDataTypeAttribute;

public class GenericDataTypeAttributeRepository extends ParetoNativeRepository<GenericDataTypeAttribute> {
  
  private static final String RELATIVE_URL = "/generic-data-type-attribute";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public GenericDataTypeAttribute get(UUID id) throws Exception {
    return super.findOne(GenericDataTypeAttribute.class, new HashMap<>(Map.of("id", id)));
  }

  public GenericDataTypeAttribute findOne(UUID idGenericDataType, String name) throws Exception {
    return super.findOne(GenericDataTypeAttribute.class, new HashMap<>(Map.of("idGenericDataType", idGenericDataType, "name", name)));
  }
  
  public List<GenericDataTypeAttribute> find(Map<String, Object> params) throws Exception {
    return super.find(GenericDataTypeAttribute.class, params);
  }
    
  public ApiResponse save(GenericDataTypeAttributePostApiRequest request) throws Exception {
    return super.post(toParams(request));
  }  
  
  public ApiResponse save(GenericDataTypeAttributePutApiRequest request) throws Exception {
    return super.put(toParams(request));
  } 

  public ApiResponse delete(GenericDataTypeAttributeDeleteApiRequest request) throws Exception {
    return super.delete(toParams(request));
  }
}