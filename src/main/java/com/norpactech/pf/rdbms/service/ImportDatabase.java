package com.norpactech.pf.rdbms.service;

import java.sql.SQLException;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.pf.rdbms.config.ParetoAPI;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.LimitOptionsBuilder;
import schemacrawler.schemacrawler.LoadOptionsBuilder;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.utility.SchemaCrawlerUtility;
import us.fatehi.utility.LoggingConfig;
import us.fatehi.utility.datasource.DatabaseConnectionSource;
import us.fatehi.utility.datasource.DatabaseConnectionSources;
import us.fatehi.utility.datasource.MultiUseUserCredentials;

public class ImportDatabase {

  private static final Logger logger = LoggerFactory.getLogger(ImportDatabase.class);

  public static void importDatabase(String context, String username, String password, String dbSchema) throws SQLException {

    logger.info("Import Database Beginning with Schema: " + ParetoAPI.schema);

    // Set log level
    new LoggingConfig(Level.OFF);

    // Create the options
    final LimitOptionsBuilder limitOptionsBuilder =
        LimitOptionsBuilder.builder()
            .includeSchemas("ub"::equals); // Directly compare the schema name

    final LoadOptionsBuilder loadOptionsBuilder =
        LoadOptionsBuilder.builder()
            .withSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
    
    final SchemaCrawlerOptions options =
        SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
            .withLimitOptions(limitOptionsBuilder.toOptions())
            .withLoadOptions(loadOptionsBuilder.toOptions());

    // Get the schema definition
    final Catalog catalog = SchemaCrawlerUtility.getCatalog(getDataSource(username, password, dbSchema), options);
    
    
    
    

    for (final Schema schema : catalog.getSchemas()) {
      System.out.println(schema);
      for (final Table table : catalog.getTables(schema)) {
        System.out.print("o--> " + table);
        for (final Column column : table.getColumns()) {
          System.out.printf("     o--> %s (%s) %s %n", column, column.getType(), column.getSize());
        }
      }
    }
    logger.info("Import Database Completed with Schema: " + ParetoAPI.schema);
  }
  
  private static DatabaseConnectionSource getDataSource(String username, String password, String dbSchema) {
    final String connectionUrl = "jdbc:mysql://localhost:3306/" + dbSchema;
    return DatabaseConnectionSources.newDatabaseConnectionSource(
        connectionUrl, new MultiUseUserCredentials(username, password));
  }
}  