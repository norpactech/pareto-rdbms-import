package com.norpactech.pf.rdbms.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.norpactech.pf.rdbms.api.model.Schema;

public class SchemaRepository extends ParetoApiRepository<Schema> {

  private static final String RELATIVE_URL = "/schema";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public Schema findOne(UUID idTenant, String name) throws Exception {
    return findOne(Schema.class, new HashMap<>(Map.of("idTenant", idTenant.toString(), "name", name)));
  }  
}
