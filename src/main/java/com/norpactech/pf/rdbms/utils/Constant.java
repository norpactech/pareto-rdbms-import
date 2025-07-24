package com.norpactech.pf.rdbms.utils;

import java.time.format.DateTimeFormatter;

public class Constant {

  public static final String SYSTEM_TENANT = "system";
  
  public static final String ONE_TO_MANY = "1:M";
  public static final String MANY_TO_ONE = "M:1";
  public static final String MANY_TO_MANY = "M:M";
  
  public static final String PRIMARY_KEY = "pk";
  public static final String FOREIGN_KEY = "fk";
  
  public static final String COMPOSITION = "COMPOSITION";
  public static final String AGGREGATION = "AGGREGATION";
  
  public static final String LIMIT = "limit";
  public static final String PAGE = "page";
  public static final String OFFSET = "offset";
  public static final String SORT_COLUMN = "sortColumn";
  public static final String SORT_DIRECTION = "sortDirection";
  
  public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); 
}
