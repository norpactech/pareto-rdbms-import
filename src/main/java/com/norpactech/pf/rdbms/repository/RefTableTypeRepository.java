package com.norpactech.pf.rdbms.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.norpactech.pf.rdbms.api.model.RefTableType;

public class RefTableTypeRepository extends ParetoApiRepository<RefTableType> {

  private static final String RELATIVE_URL = "/ref-table-type";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public RefTableType findOne(String name) throws Exception {
    return findOne(RefTableType.class, new HashMap<>(Map.of("name", name)));
  } 
  
  public List<RefTableType> findAll() throws Exception {
    return find(RefTableType.class, new HashMap<>());
  }    
}