package com.norpactech.pf.rdbms.repository;

import java.util.HashMap;
import java.util.Map;

import com.norpactech.pf.rdbms.api.model.Context;

public class ContextRepository extends ParetoApiRepository<Context> {

  private static final String RELATIVE_URL = "/context";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public Context findOne(String name) throws Exception {
    return findOne(Context.class, new HashMap<>(Map.of("name", name)));
  }  
}