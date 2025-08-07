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

import com.norpactech.pf.rdbms.dto.PropertyPostApiRequest;
import com.norpactech.pf.rdbms.dto.PropertyPutApiRequest;
import com.norpactech.pf.rdbms.model.Property;
import com.norpactech.pf.rdbms.utils.ApiResponse;

public class PropertyRepository extends ParetoApiRepository<Property> {

  private static final String RELATIVE_URL = "/property";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public Property findOne(UUID idDataObject, String name) throws Exception {
    return findOne(Property.class, new HashMap<>(Map.of("idDataObject", idDataObject.toString(), "name", name)));
  } 
  
  public List<Property> findAll(UUID idDataObject) throws Exception {
    return find(Property.class, new HashMap<>(Map.of("idSchema", idDataObject.toString())));
  }   
  
  public void save(PropertyPostApiRequest request) throws Exception {
    
    ApiResponse response = post(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Property '" + request.getName() + "' was not saved! Terminating...");
    }
  }  
  
  public void save(PropertyPutApiRequest request) throws Exception {
    
    ApiResponse response = put(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Property '" + request.getName() + "' was not saved! Terminating...");
    }
  }    
}