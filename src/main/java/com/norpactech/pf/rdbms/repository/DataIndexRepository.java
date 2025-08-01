package com.norpactech.pf.rdbms.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.norpactech.pf.rdbms.api.model.DataIndex;
import com.norpactech.pf.rdbms.dto.DataIndexPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataIndexPutApiRequest;
import com.norpactech.pf.rdbms.utils.ApiResponse;

public class DataIndexRepository extends ParetoApiRepository<DataIndex> {

  private static final String RELATIVE_URL = "/data-index";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public DataIndex findOne(UUID idDataObject, String name) throws Exception {
    
    return findOne(DataIndex.class, new HashMap<>(Map.of("idDataObject", idDataObject.toString(), "name", name)));
  } 
  
  public List<DataIndex> findAll(UUID idDataObject) throws Exception {
    return find(DataIndex.class, new HashMap<>(Map.of("idDataObject", idDataObject.toString())));
  }   
  
  public void save(DataIndexPostApiRequest request) throws Exception {
    
    ApiResponse response = post(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Data Index was not saved! Terminating...");
    }
  }  
  
  public void save(DataIndexPutApiRequest request) throws Exception {
    
    ApiResponse response = put(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Data Index was not saved! Terminating...");
    }
  }    
}