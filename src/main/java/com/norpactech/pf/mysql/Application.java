package com.norpactech.pf.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.pf.mysql.config.Datasource;
import com.norpactech.pf.mysql.config.ParetoAPI;
import com.norpactech.pf.mysql.service.ImportDatabase;

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

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/ub";
    String username = "root";
    String password = "password";
    
    String apiUsername = "scott@norpactech.com";
    String apiPassword = "G00seEgg!";
    // String tokenUrl = "https://dev.api.paretofactory.com/access-token";
    String tokenUrl = "http://localhost:8087/access-token";
    
    String tenant = "norpac"; 
    String schema = "unitedbins"; 
    String context = "mysql";
    
    try {
      logger.info("Pareto Factory Import Beginning");
      Datasource.connect(driver, url, username, password);
      
      ParetoAPI.configure(tokenUrl, tenant, schema, apiUsername, apiPassword);
      ImportDatabase.importDatabase(context);
      
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
