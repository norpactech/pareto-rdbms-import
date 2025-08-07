package com.norpactech.pf.rdbms.repository;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.util.HashMap;
import java.util.Map;

import com.norpactech.pf.rdbms.model.Context;

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