package com.norpactech.pf.rdbms.repository;

import java.util.HashMap;
import java.util.Map;

import com.norpactech.pf.rdbms.model.Tenant;

public class TenantRepository extends ParetoApiRepository<Tenant> {
  
  private static final String RELATIVE_URL = "/tenant";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public Tenant findOne(String name) throws Exception {
    return findOne(Tenant.class, new HashMap<>(Map.of("name", name)));
  }  
}
