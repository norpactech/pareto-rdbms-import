package com.norpactech.pf.rdbms.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.norpactech.pf.rdbms.api.model.Schema;
import com.norpactech.pf.rdbms.api.model.Tenant;
import com.norpactech.pf.rdbms.enums.EnumStatus;
import com.norpactech.pf.rdbms.utils.ApiGetRequest;
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
      String tokenUrl, 
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
    jwt = AuthUtils.getJwt(host + "/" + tokenUrl, jwtRequest);
    logger.info("User '{}' Signed In", jwtRequest.getEmail());
    
    ApiGetRequest tenantGetRequest = new ApiGetRequest("/tenant", new HashMap<>(Map.of("name",  tenantName)));
    ApiResponse tenantResponse = NetUtils.get(tenantGetRequest);

    if (tenantResponse.getData() == null) {
      throw new Exception("'" + ParetoAPI.tenant + "' Tenant not found. Terminating!");
    }
    tenant = tenantResponse.readObject(Tenant.class);
    logger.info("Tenant: '{}' Initialized", tenant.getName());
    
    ApiGetRequest schemaGetRequest = new ApiGetRequest("/schema", new HashMap<>(Map.of("name",  schemaName)));
    ApiResponse schemaResponse = NetUtils.get(schemaGetRequest);

    if (schemaResponse.getData() == null) {
      throw new Exception("'" + ParetoAPI.tenant + "' Tenant not found. Terminating!");
    }
    schema = schemaResponse.readObject(Schema.class);
    dbSchema = schema.getDatabase();
    logger.info("Schema: '{}' Initialized with database schema: '{}'", schema.getName(), schema.getDatabase());
    
    logger.info("Pareto API Successfully Configured");
  }  
  
  public static String getJWT() {
    return jwt;
  }
}
