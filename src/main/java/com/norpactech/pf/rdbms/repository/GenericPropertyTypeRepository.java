package com.norpactech.pf.rdbms.repository;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.norpactech.pf.rdbms.model.GenericPropertyType;

public class GenericPropertyTypeRepository extends ParetoApiRepository<GenericPropertyType> {
  
  private static final String RELATIVE_URL = "/generic-property-type";

  @Override
  protected String getRelativeURL() {
    return RELATIVE_URL;
  }

  public GenericPropertyType findOne(UUID idGenericDataType, String name) throws Exception {
    return findOne(GenericPropertyType.class, new HashMap<>(Map.of("idGenericDataType", idGenericDataType.toString(), "name", name)));
  }  
}
