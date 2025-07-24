package com.norpactech.pf.rdbms.utils;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.norpactech.pf.rdbms.config.Datasource;
import com.norpactech.pf.rdbms.vo.JwtRequestVO;

import okhttp3.OkHttpClient;

public class AuthUtils {

  private static final Logger logger = LoggerFactory.getLogger(Datasource.class);

  public static String getJwt(String tokenUrl, JwtRequestVO jwtRequest) throws Exception {

    URL url = new URL(tokenUrl);
    String accessToken = null;
    okhttp3.Response response = null;

    try { 
      OkHttpClient client = new OkHttpClient().newBuilder().build();
      okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
          .add("username", jwtRequest.getEmail())  // Assuming email is used as username
          .add("password", jwtRequest.getPassword())
          .build();

      okhttp3.Request request = new okhttp3.Request.Builder()
          .url(url)
          .post(formBody)
          .addHeader("Accept", "*/*")
          .addHeader("Content-Type", "application/x-www-form-urlencoded")
          .build();
          
      response = client.newCall(request).execute();     

      JsonObject jsonObject = new Gson().fromJson(new String(response.body().bytes()), JsonObject.class);
      JsonElement token = jsonObject.get("access_token");   
      accessToken = token.toString().replace("\"", "");
      logger.error("Access Token Successfully Retrieved");
    } 
    catch (Exception e) {
      logger.error("Failed to Retive Token for User: {}. {}", jwtRequest.getEmail(), e.getMessage());
    }
    if (StringUtils.isEmpty(accessToken)) {
      logger.error("Failed to Retive Token for User: {}. {}", jwtRequest.getEmail(), "Access token is empty");
      throw new RuntimeException("Failed to retrieve access token");
    }
    return accessToken;
  }  
}
