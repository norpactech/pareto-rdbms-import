package com.norpactech.pf.rdbms.utils;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.time.format.DateTimeFormatter;

public class Constant {

  public static final String SYSTEM_TENANT = "system";
  public static final String SCHEMA_CRAWLER_CONTEXT = "schemacrawler";
    
  public static final String CARDINALITY_TABLE_TYPE = "cardinality";
  public static final String CARDINALITY_STRENGTH_TABLE_TYPE = "cardinality_strength";
  
  public static final String INDEX_TYPE = "index_type";
  public static final String UNIQUE_INDEX = "Unique";
  public static final String INDEX = "Index";

  public static final String SORT_ORDER = "sort_order";
  public static final String ASCENDING = "Ascending";
  public static final String DESCENDING = "Descending";
    
  public static final String ONE_TO_MANY = "1:M";
  public static final String MANY_TO_ONE = "M:1";
  public static final String MANY_TO_MANY = "M:M";
  
  public static final String PRIMARY_KEY = "pk";
  public static final String FOREIGN_KEY = "fk";
  
  public static final String COMPOSITION = "Composition";
  public static final String AGGREGATION = "Aggregation";
  
  public static final String LIMIT = "limit";
  public static final String PAGE = "page";
  public static final String OFFSET = "offset";
  public static final String SORT_COLUMN = "sortColumn";
  public static final String SORT_DIRECTION = "sortDirection";
  
  public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); 
}
