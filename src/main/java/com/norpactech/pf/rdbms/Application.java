package com.norpactech.pf.rdbms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.pf.rdbms.config.Datasource;
import com.norpactech.pf.rdbms.config.ParetoAPI;
import com.norpactech.pf.rdbms.service.ImportDatabase;

/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 * 
 * This software is proprietary and may not be used, copied, modified, or distributed 
 * without permission. It is provided "as is" without warranties of any kind. 
 * 
 * For details, see the LICENSE file in this project root.
 */
public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Datasource.class);
  
  public static void main(String[] args) throws Exception {

    String username = "scott@norpactech.com";
    String password = "G00seEgg!";
    String host = "http://localhost:8087";
    String tokenUri = "access-token";
    String apiVersion = "v01";
    
    
    String tenant = "norpac"; 
    String schema = "unitedbins"; 
    String context = "mysql";
    
    String dbUsername = "root";
    String dbPassword = "password";
    
    try {
      logger.info("Pareto Factory Import Beginning");
      ParetoAPI.configure(host, apiVersion, tokenUri, tenant, schema, username, password);
      
      ImportDatabase.importDatabase(context, dbUsername, dbPassword, ParetoAPI.dbSchema);
      
      logger.info("Pareto Factory Import Completed Successfully");
      System.exit(0);
    }
    catch (Exception e) {
      logger.info("Pareto Factory Import Terminated Unexpectedly: {}", e.getMessage());
      System.exit(1);
    }
    finally {
      if (Datasource.getConnection() != null) {
        Datasource.disconnect();
      }
    }
  }
}
