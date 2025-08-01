package com.norpactech.pf.rdbms.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.norpactech.pf.rdbms.api.model.RefTables;

public class RefTablesRepository extends ParetoApiRepository<RefTables> {

  private static final String RELATIVE_URL = "/ref-tables";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public RefTables findOne(UUID idRefTableType, String name) throws Exception {
    return findOne(RefTables.class, new HashMap<>(Map.of("idRefTableType", idRefTableType.toString(), "name", name)));
  }
}