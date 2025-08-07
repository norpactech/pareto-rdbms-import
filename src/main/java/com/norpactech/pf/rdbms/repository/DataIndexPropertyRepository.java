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

import com.norpactech.pf.rdbms.dto.DataIndexPropertyPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataIndexPropertyPutApiRequest;
import com.norpactech.pf.rdbms.model.DataIndexProperty;
import com.norpactech.pf.rdbms.utils.ApiResponse;

public class DataIndexPropertyRepository extends ParetoApiRepository<DataIndexProperty> {

  private static final String RELATIVE_URL = "/data-index-property";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public DataIndexProperty findOne(UUID idDataIndex, UUID idProperty) throws Exception {
    
    return findOne(DataIndexProperty.class, new HashMap<>(Map.of("idDataIndex", idDataIndex.toString(), "idProperty", idProperty.toString())));
  } 
  
  public List<DataIndexProperty> findAll(UUID idDataObject) throws Exception {
    return find(DataIndexProperty.class, new HashMap<>(Map.of("idDataIndex", idDataObject.toString())));
  }   
  
  public void save(DataIndexPropertyPostApiRequest request) throws Exception {
    
    ApiResponse response = post(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Data Index Property was not saved! Terminating...");
    }
  }  
  
  public void save(DataIndexPropertyPutApiRequest request) throws Exception {
    
    ApiResponse response = put(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Data Index Property was not saved! Terminating...");
    }
  }    
}