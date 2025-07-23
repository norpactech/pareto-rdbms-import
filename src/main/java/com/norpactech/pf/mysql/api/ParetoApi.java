package com.norpactech.pf.mysql.api;

import com.norpactech.pf.mysql.exception.EmptyRequestFileException;

public class ParetoApi {

  public static int downloadRequest(String requestFile) throws Exception {

    if (requestFile == null || requestFile.isEmpty()) {
      throw new EmptyRequestFileException();
    }

    
    
    
    return 0;
  }
  
  
  
  
  
  
}
