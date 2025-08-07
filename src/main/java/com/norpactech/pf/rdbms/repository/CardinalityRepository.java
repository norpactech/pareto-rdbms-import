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

import com.norpactech.pf.rdbms.dto.CardinalityPostApiRequest;
import com.norpactech.pf.rdbms.dto.CardinalityPutApiRequest;
import com.norpactech.pf.rdbms.model.Cardinality;
import com.norpactech.pf.rdbms.utils.ApiResponse;

public class CardinalityRepository extends ParetoApiRepository<Cardinality> {

  private static final String RELATIVE_URL = "/cardinality";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public Cardinality findOne(UUID idDataObject, UUID idProperty) throws Exception {
    
    return findOne(Cardinality.class, new HashMap<>(Map.of("idDataObject", idDataObject.toString(), "idProperty", idProperty.toString())));
  } 
  
  public List<Cardinality> findAll(UUID idDataObject) throws Exception {
    return find(Cardinality.class, new HashMap<>(Map.of("idDataObject", idDataObject.toString())));
  }   
  
  public void save(CardinalityPostApiRequest request) throws Exception {
    
    ApiResponse response = post(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Cardinality was not saved! Terminating...");
    }
  }  
  
  public void save(CardinalityPutApiRequest request) throws Exception {
    
    ApiResponse response = put(toParams(request));
    
    if (response.getData() == null) {
      throw new Exception("Cardinality was not saved! Terminating...");
    }
  }    
}