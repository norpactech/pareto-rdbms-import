package com.norpactech.pf.rdbms.utils;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.norpactech.pf.rdbms.config.ParetoAPI;

import okhttp3.OkHttpClient;

public class NetUtils {

  final static Logger logger = LoggerFactory.getLogger(NetUtils.class);

  public static ApiResponse health() throws Exception {

    URL url = new URL(ParetoAPI.host + "/health");

    okhttp3.Response response = null;
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
        .url(url)
        .get()
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json");

    okhttp3.Request request = requestBuilder.build();
    response = client.newCall(request).execute();       

    int responseCode = response.code();
    if (responseCode > 299) {
      throw new Exception("GET Request Failed: " + response.message());
    }
    Object retVal = new String(response.body().bytes());
    return new ApiResponse(retVal);
  }    
  
  public static ApiResponse get(ApiGetRequest apiGetRequest) throws Exception {

    String version = ParetoAPI.apiVersion == null ? "" : "/" + ParetoAPI.apiVersion;
    String queryString = TextUtils.toQueryString(apiGetRequest.getParams());
    URL url = new URL(ParetoAPI.host + version + apiGetRequest.getUri() + queryString.toString());

    okhttp3.Response response = null;
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
        .url(url)
        .get()
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + ParetoAPI.jwt);
    
    okhttp3.Request request = requestBuilder.build();
    response = client.newCall(request).execute();       

    int responseCode = response.code();
    if (responseCode > 299) {
      throw new Exception("GET Request Failed: " + response.message());
    }
    return new Gson().fromJson(response.body().string(), ApiResponse.class);
  }  
  
  public static ApiResponse find(ApiGetRequest apiGetRequest) throws Exception {

    String version = ParetoAPI.apiVersion == null ? "" : "/" + ParetoAPI.apiVersion;
    String queryString = TextUtils.toQueryString(apiGetRequest.getParams());
    URL url = new URL(ParetoAPI.host + version + apiGetRequest.getUri() + queryString.toString());

    okhttp3.Response response = null;
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
        .url(url)
        .get()
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + ParetoAPI.jwt);
    
    okhttp3.Request request = requestBuilder.build();
    response = client.newCall(request).execute();       

    int responseCode = response.code();
    if (responseCode > 299) {
      throw new Exception("GET Request Failed: " + response.message());
    }
    return new Gson().fromJson(response.body().string(), ApiResponse.class);
  }    
  
}