package com.norpactech.pf.rdbms.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Datasource {

  private static final Logger logger = LoggerFactory.getLogger(Datasource.class);
  static Connection connection = null;
  
  public static void connect(String driver, String url, String username, String password) throws Exception {

    try {
      Class.forName(driver);
      connection = DriverManager.getConnection(url, username, password);
      logger.info("Database connection established successfully");
    } 
    catch (SQLException e) {
      e.printStackTrace();
      throw new Exception("Failed to connect to MySQL database: " + url);
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new Exception("Driver class not found: " + driver);
    }
  }  

  public static Connection getConnection() {
    return connection;
  }
  
  public static void disconnect() throws Exception {
  
    if (connection != null) {
      try {
        connection.close();
        logger.info("Database connection closed successfully");
      } 
      catch (SQLException e) {
        e.printStackTrace();
        throw new Exception("Disconnect Error: " + e.getMessage());
      }
    }
    else {
      throw new Exception("Database disconnect attempted on null connection");
    }
  }
}
