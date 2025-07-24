package com.norpactech.pf.rdbms.utils;

import java.util.HashMap;
import java.util.Map;
/**
 * Send a Get Request
 */
public class ApiGetRequest {

  String uri;
  Map<String, String> params = new HashMap<String, String>();
  
  public ApiGetRequest(String uri, Map<String, String> params) {
    
    this.uri = uri;
    if (params != null) {
      this.params = params;
    }
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }
}
