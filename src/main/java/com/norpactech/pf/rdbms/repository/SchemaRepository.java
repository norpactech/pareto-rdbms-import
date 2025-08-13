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
import com.norpactech.pf.rdbms.dto.SchemaPutApiRequest;
import com.norpactech.pf.rdbms.dto.SchemaPostApiRequest;
import com.norpactech.pf.rdbms.dto.SchemaDeleteApiRequest;

import com.norpactech.pf.rdbms.model.Schema;

public class SchemaRepository extends ParetoNativeRepository<Schema> {
  
  private static final String RELATIVE_URL = "/schema";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public Schema get(UUID id) throws Exception {
    return super.findOne(Schema.class, new HashMap<>(Map.of("id", id)));
  }

  public Schema findOne(UUID idTenant, String name) throws Exception {
    return super.findOne(Schema.class, new HashMap<>(Map.of("idTenant", idTenant, "name", name)));
  }
  
  public List<Schema> find(Map<String, Object> params) throws Exception {
    return super.find(Schema.class, params);
  }
    
  public ApiResponse save(SchemaPostApiRequest request) throws Exception {
    return super.post(toParams(request));
  }  
  
  public ApiResponse save(SchemaPutApiRequest request) throws Exception {
    return super.put(toParams(request));
  } 

  public ApiResponse delete(SchemaDeleteApiRequest request) throws Exception {
    return super.delete(toParams(request));
  }
}