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
import com.norpactech.pf.rdbms.dto.CardinalityPostApiRequest;
import com.norpactech.pf.rdbms.dto.CardinalityDeleteApiRequest;
import com.norpactech.pf.rdbms.dto.CardinalityPutApiRequest;

import com.norpactech.pf.rdbms.model.Cardinality;

public class CardinalityRepository extends ParetoNativeRepository<Cardinality> {
  
  private static final String RELATIVE_URL = "/cardinality";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public Cardinality get(UUID id) throws Exception {
    return super.findOne(Cardinality.class, new HashMap<>(Map.of("id", id)));
  }

  public Cardinality findOne(UUID idProperty, UUID idDataObject) throws Exception {
    return super.findOne(Cardinality.class, new HashMap<>(Map.of("idProperty", idProperty, "idDataObject", idDataObject)));
  }
  
  public List<Cardinality> find(Map<String, Object> params) throws Exception {
    return super.find(Cardinality.class, params);
  }
    
  public ApiResponse save(CardinalityPostApiRequest request) throws Exception {
    return super.post(toParams(request));
  }  
  
  public ApiResponse save(CardinalityPutApiRequest request) throws Exception {
    return super.put(toParams(request));
  } 

  public ApiResponse delete(CardinalityDeleteApiRequest request) throws Exception {
    return super.delete(toParams(request));
  }
}