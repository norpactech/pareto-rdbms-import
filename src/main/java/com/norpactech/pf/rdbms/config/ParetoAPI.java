package com.norpactech.pf.rdbms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.pf.rdbms.utils.AuthUtils;
import com.norpactech.pf.rdbms.vo.JwtRequestVO;

public class ParetoAPI {

  private static final Logger logger = LoggerFactory.getLogger(ParetoAPI.class);

  public static String jwt;
  public static String tenant;
  public static String schema;

  public static void configure(String tokenUrl, String thatTenant, String thatSchema, String username, String password) throws Exception {

    JwtRequestVO jwtRequest = new JwtRequestVO(username, password);
    jwt = AuthUtils.getJwt(tokenUrl, jwtRequest);

    if (thatTenant != null) {
      tenant = thatTenant;
    }
    else {
      throw new Exception ("Null Tenant!");
    }

    if (thatSchema != null) {
      schema = thatSchema;
    }
    else {
      throw new Exception ("Null Schema!");
    }
    logger.info("Pareto API Successfully Configured");
  }  
}
