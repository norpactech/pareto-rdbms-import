package com.norpactech.pf.rdbms.repository;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.norpactech.pf.rdbms.config.ParetoAPI;
import com.norpactech.pf.rdbms.utils.ApiGetRequest;
import com.norpactech.pf.rdbms.utils.ApiResponse;
import com.norpactech.pf.rdbms.utils.TextUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public abstract class ParetoApiRepository<T> {

  protected abstract String getRelativeURL();

  public T findOne(Class<T> entityType, Map<String, String> queryParams) throws Exception {
    ApiGetRequest request = new ApiGetRequest(entityType, getRelativeURL(), queryParams);
    ApiResponse response = get(request);

    if (response.getData() == null) {
      return null;
    }
    return response.readObject(entityType);
  }
  
  public List<T> find(Class<T> entityType, Map<String, String> queryParams) throws Exception {
    ApiGetRequest request = new ApiGetRequest(entityType, getRelativeURL(), queryParams);
    request.setUri(getRelativeURL() + "/find");
    
    ApiResponse response = get(request);

    if (response.getData() == null) {
      return new ArrayList<>();
    }
    return response.readList(entityType);
  }
  
  public T post(Class<T> entityType, Map<String, Object> params) throws Exception {
    

    return null;
  }  
  
  public ApiResponse get(ApiGetRequest apiGetRequest) throws Exception {

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
  
  public ApiResponse post(Map<String, Object> apiPostRequest) throws Exception {

    String version = ParetoAPI.apiVersion == null ? "" : "/" + ParetoAPI.apiVersion;
    URL url = new URL(ParetoAPI.host + version + getRelativeURL());

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    String jsonBody = new Gson().toJson(apiPostRequest);
    RequestBody requestBody = RequestBody.create(jsonBody, MediaType.get("application/json"));

    okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
        .url(url)
        .post(requestBody)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + ParetoAPI.jwt);

    okhttp3.Request request = requestBuilder.build();
    okhttp3.Response response = client.newCall(request).execute();

    int responseCode = response.code();
    if (responseCode > 299) {
        throw new Exception("POST Request Failed: " + response.message());
    }
    return new Gson().fromJson(response.body().string(), ApiResponse.class);
  }
  
  public ApiResponse put(Map<String, Object> apiPostRequest) throws Exception {

    String version = ParetoAPI.apiVersion == null ? "" : "/" + ParetoAPI.apiVersion;
    URL url = new URL(ParetoAPI.host + version + getRelativeURL());

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    String jsonBody = new Gson().toJson(apiPostRequest);
    RequestBody requestBody = RequestBody.create(jsonBody, MediaType.get("application/json"));

    okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
        .url(url)
        .put(requestBody)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + ParetoAPI.jwt);

    okhttp3.Request request = requestBuilder.build();
    okhttp3.Response response = client.newCall(request).execute();

    int responseCode = response.code();
    if (responseCode > 299) {
        throw new Exception("POST Request Failed: " + response.message());
    }
    return new Gson().fromJson(response.body().string(), ApiResponse.class);
  }
  
  public Map<String, Object> toParams(Object request) throws IllegalAccessException {
    
    Map<String, Object> map = new LinkedHashMap<>();
    Class<?> clazz = request.getClass();

    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      map.put(field.getName(), field.get(request));
    }
    return map;
  }
}
