package com.norpactech.pf.rdbms.config;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.norpactech.pf.rdbms.enums.EnumStatus;
import com.norpactech.pf.rdbms.model.Schema;
import com.norpactech.pf.rdbms.model.Tenant;
import com.norpactech.pf.rdbms.repository.SchemaRepository;
import com.norpactech.pf.rdbms.repository.TenantRepository;
import com.norpactech.pf.rdbms.utils.ApiResponse;
import com.norpactech.pf.rdbms.utils.AuthUtils;
import com.norpactech.pf.rdbms.utils.NetUtils;
import com.norpactech.pf.rdbms.vo.JwtRequestVO;

public class ParetoAPI {

  private static final Logger logger = LoggerFactory.getLogger(ParetoAPI.class);

  public static String host;
  public static String apiVersion;
  public static String jwt;
  public static String dbSchema;

  public static Tenant tenant;
  public static Schema schema;
  
  public static void configure(
      String thatHost, 
      String thatApiVersion, 
      String tenantName, 
      String schemaName, 
      String username, 
      String password) throws Exception {

    logger.info("Configuring Pareto API...");
    
    if (thatHost != null) {
      host = thatHost;
    }
    else {
      throw new Exception ("Null Host!");
    } 
    
    if (thatApiVersion != null) {
      apiVersion = thatApiVersion;
    }
    else {
      throw new Exception ("Null API Version!");
    }
    
    ApiResponse health = NetUtils.health();
    
    JsonObject jsonObject = JsonParser.parseString(health.getData().toString()).getAsJsonObject();
    String status = jsonObject.get("status").getAsString();
    if (!status.equals(EnumStatus.OK.getName())) {
      throw new Exception("Unhealthy Server. Status: " + status); 
    }
    logger.info("Pareto API Health Status: {}", status);
    
    JwtRequestVO jwtRequest = new JwtRequestVO(username, password);
    jwt = AuthUtils.getJwt(host + "/access-token", jwtRequest);
    logger.info("User '{}' Signed In", jwtRequest.getEmail());
    
    tenant = new TenantRepository().findOne(tenantName);
    if (tenant != null) {
      logger.info("Tenant: '{}' Initialized", tenant.getName());
    }
    else {
      throw new Exception("Tenant '" + tenantName + "' was not initialized! Terminating...");
    }
    schema = new SchemaRepository().findOne(tenant.getId(), schemaName);
    if (tenant != null) {
      logger.info("Schema: '{}' Initialized", schema.getName());
    }
    else {
      throw new Exception("Schema '" + schemaName + "' was not initialized! Terminating...");
    }
    dbSchema = schema.getDatabase();
    logger.info("Schema: '{}' Initialized with database schema: '{}'", schema.getName(), schema.getDatabase());
    
    logger.info("Pareto API Successfully Configured");
  }  
}
