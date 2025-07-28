package com.norpactech.pf.rdbms.service;

import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.pf.rdbms.api.model.DataObject;
import com.norpactech.pf.rdbms.config.ParetoAPI;
import com.norpactech.pf.rdbms.dto.DataObjectPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataObjectPutApiRequest;
import com.norpactech.pf.rdbms.repository.DataObjectRepository;

import schemacrawler.schema.Catalog;
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
  
  public static void importDatabase(String context, String username, String password, String dbSchema) throws Exception {

    final DataObjectRepository dataObjectRepository = new DataObjectRepository();
    
    logger.info("Import Database Beginning with Schema: " + ParetoAPI.schema);

    new LoggingConfig(Level.OFF);

    LimitOptionsBuilder limitOptionsBuilder =
      LimitOptionsBuilder.builder().includeSchemas("ub"::equals);

    LoadOptionsBuilder loadOptionsBuilder =
      LoadOptionsBuilder.builder().withSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
    
    SchemaCrawlerOptions options =
      SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
        .withLimitOptions(limitOptionsBuilder.toOptions())
        .withLoadOptions(loadOptionsBuilder.toOptions());

    Catalog catalog = SchemaCrawlerUtility.getCatalog(getDataSource(username, password, dbSchema), options);

//    List<DataObject> dataObjects = new DataObjectRepository().findAll(ParetoAPI.schema.getId());
    
    
    Schema catalogSchema = catalog.getSchemas().stream()
        .filter(schema -> schema.getCatalogName().equals(dbSchema))
        .findFirst()
        .orElse(null);

    if (catalogSchema == null) {
      throw new Exception("Schema not found in database catalog! Terminating...");
    }
      
    for (final Table table : catalog.getTables(catalogSchema)) {
      DataObject dataObject = dataObjectRepository.findOne(ParetoAPI.schema.getId(), table.getName());
      
      if (dataObject == null) {
        var request = new DataObjectPostApiRequest();
        request.setIdSchema(ParetoAPI.schema.getId());
        request.setName(table.getName());
        request.setDescription("Created " + table.getName() + " from Import");
        request.setHasIdentifier(true);
        request.setHasAudit(true);
        request.setHasActive(true);
        request.setCreatedBy("RDBMS Import Post");

        dataObjectRepository.save(request);
        logger.info("Table '" + table.getName() + "' imported.");
      }
      else {
        var request = new DataObjectPutApiRequest();
        request.setId(dataObject.getId());
        request.setName(dataObject.getName());
        request.setDescription("RDBMS Import Update");
        request.setHasIdentifier(true);
        request.setHasAudit(true);
        request.setHasActive(true);
        request.setUpdatedAt(dataObject.getUpdatedAt());
        request.setUpdatedBy("RDBMS Import Put");
        
        dataObjectRepository.save(request);
      }
      
      /*
        System.out.print("o--> " + table);
        for (final Column column : table.getColumns()) {
          System.out.printf("     o--> %s (%s) %s %n", column, column.getType(), column.getSize());
        }
      }
      */
    }
    logger.info("Import Database Completed with Schema: " + ParetoAPI.schema);
  }
  
  private static DatabaseConnectionSource getDataSource(String username, String password, String dbSchema) {
    final String connectionUrl = "jdbc:mysql://localhost:3306/" + dbSchema;
    return DatabaseConnectionSources.newDatabaseConnectionSource(
        connectionUrl, new MultiUseUserCredentials(username, password));
  }
}  