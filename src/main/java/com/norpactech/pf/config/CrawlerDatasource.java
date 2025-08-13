package com.norpactech.pf.config;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

import us.fatehi.utility.datasource.DatabaseConnectionSource;

public class CrawlerDatasource implements DatabaseConnectionSource {

  private final Connection connection;
  private Consumer<Connection> connectionInitializer;

  public CrawlerDatasource(Connection connection) {
    this.connection = connection;
  }

  @Override
  public Connection get() {
    if (connectionInitializer != null) {
      connectionInitializer.accept(connection);
    }
    return connection;
  }

  @Override
  public boolean releaseConnection(Connection connection) {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void setFirstConnectionInitializer(Consumer<Connection> connectionInitializer) {
    this.connectionInitializer = connectionInitializer;
  }

  @Override
  public void close() throws Exception {
    releaseConnection(connection);
  }
}