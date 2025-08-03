package com.norpactech.pf.rdbms.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.norpactech.pf.rdbms.model.Validation;

public class ValidationRepository extends ParetoApiRepository<Validation> {
  
  private static final String RELATIVE_URL = "/validation";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public Validation findOne(UUID idTenant, String name) throws Exception {
    return findOne(Validation.class, new HashMap<>(Map.of("idTenant", idTenant.toString(), "name", name)));
  }  
}
