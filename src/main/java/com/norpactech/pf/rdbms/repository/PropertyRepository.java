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
import com.norpactech.pf.rdbms.dto.PropertyPutApiRequest;
import com.norpactech.pf.rdbms.dto.PropertyDeleteApiRequest;
import com.norpactech.pf.rdbms.dto.PropertyPostApiRequest;

import com.norpactech.pf.rdbms.model.Property;

public class PropertyRepository extends ParetoNativeRepository<Property> {
  
  private static final String RELATIVE_URL = "/property";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public Property get(UUID id) throws Exception {
    return super.findOne(Property.class, new HashMap<>(Map.of("id", id)));
  }

  public Property findOne(UUID idDataObject, String name) throws Exception {
    return super.findOne(Property.class, new HashMap<>(Map.of("idDataObject", idDataObject, "name", name)));
  }
  
  public List<Property> find(Map<String, Object> params) throws Exception {
    return super.find(Property.class, params);
  }
    
  public ApiResponse save(PropertyPostApiRequest request) throws Exception {
    return super.post(toParams(request));
  }  
  
  public ApiResponse save(PropertyPutApiRequest request) throws Exception {
    return super.put(toParams(request));
  } 

  public ApiResponse delete(PropertyDeleteApiRequest request) throws Exception {
    return super.delete(toParams(request));
  }
}