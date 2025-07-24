package com.norpactech.pf.rdbms.api;

import com.norpactech.pf.rdbms.exception.EmptyRequestFileException;

public class ParetoApi {

  public static int downloadRequest(String requestFile) throws Exception {

    if (requestFile == null || requestFile.isEmpty()) {
      throw new EmptyRequestFileException();
    }

    
    
    
    return 0;
  }
  
  
  
  
  
  
}
