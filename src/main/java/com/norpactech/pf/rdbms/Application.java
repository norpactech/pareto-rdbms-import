package com.norpactech.pf.rdbms;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.pf.config.Datasource;
import com.norpactech.pf.config.ConfiguredAPI;
import com.norpactech.pf.rdbms.service.ImportDatabase;

public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Datasource.class);
  
  public static void main(String[] args) throws Exception {

    String username   = System.getenv("PARETO_USERNAME");
    String password   = System.getenv("PARETO_PASSWORD");
    String factoryURL = System.getenv("PARETO_FACTORY_URL");
    String apiVersion = System.getenv("PARETO_API_VERSION");
    
    String tenant = System.getenv("TENANT");
    String schema = System.getenv("SCHEMA");

    String dbUsername = System.getenv("DB_USERNAME");
    String dbPassword = System.getenv("DB_PASSWORD");
    String dbSchema   = System.getenv("DB_SCHEMA");
    
    logger.info("Beginning Pareto Build");

    if (StringUtils.isEmpty(username)) {
      logger.error("Null or empty username. Set environment variable: PARETO_BUILD_USERNAME. Terminating...");
      System.exit(1);
    }
    
    if (StringUtils.isEmpty(password)) {
      logger.error("Null or empty password. Set environment variable: PARETO_BUILD_PASSWORD. Terminating...");
      System.exit(1);
    }
    
    if (StringUtils.isEmpty(factoryURL)) {
      logger.error("Null or empty Pareto Factory URL. Set environment variable: PARETO_BUILD_FACTORY_URL. Terminating...");
      System.exit(1);
    }

    if (StringUtils.isEmpty(apiVersion)) {
      logger.error("Null or empty API Version. Set environment variable: PARETO_API_VERSION. Terminating...");
      System.exit(1);
    }    

    if (StringUtils.isEmpty(tenant)) {
      logger.error("Null or empty Tenant. Set environment variable: TENANT. Terminating...");
      System.exit(1);
    }    
    
    if (StringUtils.isEmpty(schema)) {
      logger.error("Null or empty Schema. Set environment variable: SCHEMA. Terminating...");
      System.exit(1);
    }    

    if (StringUtils.isEmpty(dbUsername)) {
      logger.error("Null or empty Database Username. Set environment variable: DB_USERNAME. Terminating...");
      System.exit(1);
    }    
 
    if (StringUtils.isEmpty(dbPassword)) {
      logger.error("Null or empty Database Password. Set environment variable: DB_PASSWORD. Terminating...");
      System.exit(1);
    }    
    
    try {
      logger.info("Pareto Factory Import Beginning");
      
      ConfiguredAPI.configure(factoryURL, apiVersion, tenant, schema, username, password);
      ImportDatabase.importDatabase(dbUsername, dbPassword, dbSchema);
      
      logger.info("Pareto Factory Import Completed Successfully");
      System.exit(0);
    }
    catch (Exception e) {
      logger.info("Pareto Factory Import Terminated Unexpectedly: {}", e);
      System.exit(1);
    }
    finally {
      if (Datasource.getConnection() != null) {
        Datasource.disconnect();
      }
    }
  }
}
