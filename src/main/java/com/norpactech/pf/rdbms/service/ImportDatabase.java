package com.norpactech.pf.rdbms.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.pf.rdbms.api.model.Cardinality;
import com.norpactech.pf.rdbms.api.model.Context;
import com.norpactech.pf.rdbms.api.model.ContextDataType;
import com.norpactech.pf.rdbms.api.model.DataIndex;
import com.norpactech.pf.rdbms.api.model.DataIndexProperty;
import com.norpactech.pf.rdbms.api.model.DataObject;
import com.norpactech.pf.rdbms.api.model.GenericPropertyType;
import com.norpactech.pf.rdbms.api.model.Property;
import com.norpactech.pf.rdbms.api.model.RefTableType;
import com.norpactech.pf.rdbms.api.model.RefTables;
import com.norpactech.pf.rdbms.api.model.Validation;
import com.norpactech.pf.rdbms.config.ParetoAPI;
import com.norpactech.pf.rdbms.dto.CardinalityPostApiRequest;
import com.norpactech.pf.rdbms.dto.CardinalityPutApiRequest;
import com.norpactech.pf.rdbms.dto.DataIndexPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataIndexPropertyPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataIndexPropertyPutApiRequest;
import com.norpactech.pf.rdbms.dto.DataIndexPutApiRequest;
import com.norpactech.pf.rdbms.dto.DataObjectPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataObjectPutApiRequest;
import com.norpactech.pf.rdbms.dto.PropertyPostApiRequest;
import com.norpactech.pf.rdbms.dto.PropertyPutApiRequest;
import com.norpactech.pf.rdbms.repository.CardinalityRepository;
import com.norpactech.pf.rdbms.repository.ContextDataTypeRepository;
import com.norpactech.pf.rdbms.repository.ContextRepository;
import com.norpactech.pf.rdbms.repository.DataIndexPropertyRepository;
import com.norpactech.pf.rdbms.repository.DataIndexRepository;
import com.norpactech.pf.rdbms.repository.DataObjectRepository;
import com.norpactech.pf.rdbms.repository.GenericPropertyTypeRepository;
import com.norpactech.pf.rdbms.repository.PropertyRepository;
import com.norpactech.pf.rdbms.repository.RefTableTypeRepository;
import com.norpactech.pf.rdbms.repository.RefTablesRepository;
import com.norpactech.pf.rdbms.repository.ValidationRepository;
import com.norpactech.pf.rdbms.utils.Constant;
import com.norpactech.pf.rdbms.utils.TextUtils;
import com.norpactech.pf.rdbms.vo.ForeignKeyVO;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.ColumnReference;
import schemacrawler.schema.ForeignKey;
import schemacrawler.schema.Index;
import schemacrawler.schema.IndexColumn;
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
  private static final ContextRepository contextRepository = new ContextRepository();
  private static final ContextDataTypeRepository contextDataTypeRepository = new ContextDataTypeRepository();
  private static final DataObjectRepository dataObjectRepository = new DataObjectRepository();
  private static final PropertyRepository propertyRepository = new PropertyRepository();
  private static final ValidationRepository validationRepository = new ValidationRepository();
  private static final GenericPropertyTypeRepository genericPropertyTypeRepository = new GenericPropertyTypeRepository();
  private static final RefTableTypeRepository refTableTypeRepository = new RefTableTypeRepository();
  private static final RefTablesRepository refTablesRepository = new RefTablesRepository();
  private static final CardinalityRepository cardinalityRepository = new CardinalityRepository();
  private static final DataIndexRepository dataIndexRepository = new DataIndexRepository();
  private static final DataIndexPropertyRepository dataIndexPropertyRepository = new DataIndexPropertyRepository();
  
  public static void importDatabase(String context, String username, String password, String dbSchema) throws Exception {
    
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
    var paretoContext = contextRepository.findOne(Constant.SCHEMA_CRAWLER_CONTEXT);
    if (paretoContext == null) {
      throw new Exception("Pareto Context Not Found! Terminating...");
    }
    Collection<Table> tables = catalog.getTables(catalogSchema);
    importTables(tables, paretoContext);    
    importCardinality(tables);    
    importIndexes(tables);    
    logger.info("Import Database Completed with Schema: " + ParetoAPI.schema);
  }

  private static void importTables(Collection<Table> tables, Context paretoContext) throws Exception {

    for (final Table table : tables) {
      DataObject dataObject = dataObjectRepository.findOne(ParetoAPI.schema.getId(), table.getName());

      boolean hasIdentifier = false;
      boolean hasAudit = false;
      boolean hasActive = false;

      var auditFields = new ArrayList<String>();

      for (final Column column : table.getColumns()) {
        if (column.getName().equalsIgnoreCase("ID")) {
          hasIdentifier = true;
        }
        else if (column.getName().equalsIgnoreCase("IS_ACTIVE")) {
          hasActive = true;
        }
        else if (column.getName().equalsIgnoreCase("CREATED_AT")) {
          auditFields.add(column.getName());
        }
        else if (column.getName().equalsIgnoreCase("CREATED_BY")) {
          auditFields.add(column.getName());
        }
        else if (column.getName().equalsIgnoreCase("UPDATED_AT")) {
          auditFields.add(column.getName());
        }
        else if (column.getName().equalsIgnoreCase("UPDATED_BY")) {
          auditFields.add(column.getName());
        }
      }
      if (auditFields.size() == 4) {      
        hasAudit = true;
      }

      if (dataObject == null) {
        var request = new DataObjectPostApiRequest();
        request.setIdSchema(ParetoAPI.schema.getId());
        request.setName(table.getName());
        request.setDescription("Created " + table.getName() + " from Import");
        request.setHasIdentifier(hasIdentifier);
        request.setHasAudit(hasAudit);
        request.setHasActive(hasActive);
        request.setCreatedBy("RDBMS Import Post");

        dataObjectRepository.save(request);
      }
      else {
        var request = new DataObjectPutApiRequest();
        request.setId(dataObject.getId());
        request.setName(dataObject.getName());
        request.setDescription("RDBMS Import Update");
        request.setHasIdentifier(hasIdentifier);
        request.setHasAudit(hasAudit);
        request.setHasActive(hasActive);
        request.setUpdatedAt(dataObject.getUpdatedAt());
        request.setUpdatedBy("RDBMS Import Put");

        dataObjectRepository.save(request);
      }      
      importProperties(paretoContext, table);
      logger.info("DataObject '" + table.getName() + "' imported.");
    }
  }

  private static void importProperties(Context paretoContext, Table table) throws Exception {
    
    DataObject dataObject = dataObjectRepository.findOne(ParetoAPI.schema.getId(), table.getName());

    int i = 1;
    for (final Column column : table.getColumns()) {
      // Context Data Type
      ContextDataType contextDataType = contextDataTypeRepository.findOne(paretoContext.getId(), column.getType().getJavaSqlType().getName().toLowerCase());
      if (contextDataType == null) {
        throw new Exception("Unsupported Context Data Type! '" + column.getType().getJavaSqlType().getName() + "' Terminating...");
      }
      UUID idGenericDataType = contextDataType.getIdGenericDataType();
      // Validation
      Validation validation = validationRepository.findOne(ParetoAPI.tenant.getId(), column.getName());
      UUID idValidation = validation == null ? null : validation.getId();
      // Generic Property Type
      GenericPropertyType genericPropertyType = genericPropertyTypeRepository.findOne(idGenericDataType, column.getName());
      UUID idGenericPropertyType = genericPropertyType == null ? null : genericPropertyType.getId();
      
      Property property = propertyRepository.findOne(dataObject.getId(), column.getName());
      
      if (property == null) {
        var request = new PropertyPostApiRequest();
        request.setIdDataObject(dataObject.getId());
        request.setIdGenericDataType(idGenericDataType);
        if (idValidation != null) {
          request.setIdValidation(idValidation);
        }
        if (idGenericPropertyType != null) {
          request.setIdGenericPropertyType(idGenericPropertyType);
        }
        request.setSequence(i++);
        request.setName(column.getName());
        request.setDescription(column.getRemarks());
        request.setIsUpdatable(true);
        request.setFkViewable(false);
        request.setLength(column.getSize());
        request.setScale(column.getDecimalDigits());
        request.setIsNullable(column.isNullable());
        request.setDefaultValue(column.getDefaultValue());
        request.setCreatedBy("RDBMS Import Post");
        propertyRepository.save(request);
      }
      else {
        var request = new PropertyPutApiRequest();
        request.setId(property.getId());
        request.setIdGenericDataType(idGenericDataType);
        if (idValidation != null) {
          request.setIdValidation(idValidation);
        }
        if (idGenericPropertyType != null) {
          request.setIdGenericPropertyType(idGenericPropertyType);
        }
        request.setSequence(i++);
        request.setName(column.getName());
        request.setDescription(column.getRemarks());
        request.setIsUpdatable(property.getIsUpdatable());
        request.setFkViewable(property.getFkViewable());
        request.setLength(column.getSize());
        request.setScale(column.getDecimalDigits());
        request.setIsNullable(column.isNullable());
        request.setDefaultValue(column.getDefaultValue());
        request.setUpdatedAt(property.getUpdatedAt());
        request.setUpdatedBy("RDBMS Import Put");
        propertyRepository.save(request);
      }
    }
  }

  private static void importCardinality(Collection<Table> tables) throws Exception {

    RefTableType cardinalityTT = refTableTypeRepository.findOne(Constant.CARDINALITY_TABLE_TYPE);
    if (cardinalityTT == null) {
      throw new Exception ("Cardinality Table Type not found! Terminating...");
    }
    RefTables oneToMany = refTablesRepository.findOne(cardinalityTT.getId(), Constant.ONE_TO_MANY);
    if (oneToMany == null) {
      throw new Exception ("M:1 Cardinality not found! Terminating...");
    }
    RefTableType cardinalityStrengthTT = refTableTypeRepository.findOne(Constant.CARDINALITY_STRENGTH_TABLE_TYPE); 
    if (cardinalityStrengthTT == null) {
      throw new Exception ("Cardinality Strength Table Type not found! Terminating...");
    }
    RefTables aggregation = refTablesRepository.findOne(cardinalityStrengthTT.getId(), Constant.AGGREGATION);
    RefTables composition = refTablesRepository.findOne(cardinalityStrengthTT.getId(), Constant.COMPOSITION);
    
    Set<ForeignKeyVO> foreignKeys = new HashSet<ForeignKeyVO>();
    
    for (Table table : tables) {      
      for (ForeignKey foreignKey : table.getForeignKeys()) {
        var references = TextUtils.lastDelimetedValue(foreignKey.getPrimaryKeyTable().getFullName(), ".");
        var tableName = TextUtils.lastDelimetedValue(foreignKey.getDependentTable().getFullName(), ".");
        
        for (ColumnReference reference : foreignKey.getColumnReferences()) {
          var foreignKeyName = TextUtils.lastDelimetedValue(reference.getForeignKeyColumn().getName(), ".");
          var primaryKeyName = TextUtils.lastDelimetedValue(reference.getPrimaryKeyColumn().getName(), ".");
          var foreignKeyVO = new ForeignKeyVO(
              tableName.replace("`", ""),  
              foreignKeyName.replace("`", ""), 
              references.replace("`", ""), 
              primaryKeyName.replace("`", ""));
          
          foreignKeys.add(foreignKeyVO);
        }
      }
    }
    for (ForeignKeyVO fk : foreignKeys) {

      DataObject tableDataObject = dataObjectRepository.findOne(ParetoAPI.schema.getId(), fk.getTable());
      if (tableDataObject == null) {
        throw new Exception ("Foreign Key Table Data Object '" + fk.getTable() + "' not Found! Terminating...");
      }
      Property foreignKeyProperty = propertyRepository.findOne(tableDataObject.getId(), fk.getForeignKey());
      if (foreignKeyProperty == null) {
        throw new Exception ("Foreign Key Property '" + fk.getForeignKey() + "' not Found! Terminating...");
      }
      DataObject referencesDataObject = dataObjectRepository.findOne(ParetoAPI.schema.getId(), fk.getReferences());
      if (referencesDataObject == null) {
        throw new Exception ("Foreign Key References Data Object '" + fk.getReferences() + "' not Found! Terminating...");
      }
      Property primaryKeyProperty = propertyRepository.findOne(referencesDataObject.getId(), fk.getPrimaryKey());
      if (primaryKeyProperty == null) {
        throw new Exception ("Primary Key Property '" + fk.getPrimaryKey() + "' not Found! Terminating...");
      }
      
      Cardinality cardinality = cardinalityRepository.findOne(referencesDataObject.getId(), foreignKeyProperty.getId());
      
      if (cardinality == null) {
        CardinalityPostApiRequest postRequest = new CardinalityPostApiRequest();
        postRequest.setIdProperty(foreignKeyProperty.getId());
        postRequest.setIdDataObject(referencesDataObject.getId());
        postRequest.setIdRtCardinality(oneToMany.getId());
        if (foreignKeyProperty.getIsNullable()) {
          postRequest.setIdRtCardinalityStrength(aggregation.getId());
        }
        else {
          postRequest.setIdRtCardinalityStrength(composition.getId());
        }
        postRequest.setHasReferencialAction(true);
        postRequest.setCreatedBy("RDBMS Import Post");
        
        cardinalityRepository.save(postRequest);
      }
      else {
        CardinalityPutApiRequest putRequest = new CardinalityPutApiRequest();
        putRequest.setId(cardinality.getId());
        putRequest.setIdRtCardinality(oneToMany.getId());
        if (foreignKeyProperty.getIsNullable()) {
          putRequest.setIdRtCardinalityStrength(cardinality.getIdRtCardinalityStrength());
        }
        else {
          putRequest.setIdRtCardinalityStrength(cardinality.getIdRtCardinalityStrength());
        }
        putRequest.setHasReferencialAction(cardinality.getHasReferencialAction());
        putRequest.setUpdatedAt(cardinality.getUpdatedAt());
        putRequest.setUpdatedBy("RDBMS Import Put");
        
        cardinalityRepository.save(putRequest);
      }
    }
  }
  
  private static void importIndexes(Collection<Table> tables) throws Exception {

    RefTableType indexType = refTableTypeRepository.findOne(Constant.INDEX_TYPE); 
    if (indexType == null) {
      throw new Exception ("Index Type Table Type not found! Terminating...");
    }
    RefTables uniqueIndex = refTablesRepository.findOne(indexType.getId(), Constant.UNIQUE_INDEX);
    RefTables nonUniqueIndex = refTablesRepository.findOne(indexType.getId(), Constant.INDEX);

    RefTableType rtSortOrder = refTableTypeRepository.findOne(Constant.SORT_ORDER); 
    if (rtSortOrder == null) {
      throw new Exception ("Sort Order Table Type not found! Terminating...");
    }
    RefTables ascending = refTablesRepository.findOne(rtSortOrder.getId(), Constant.ASCENDING);
    RefTables descending = refTablesRepository.findOne(rtSortOrder.getId(), Constant.DESCENDING);
    
    for (Table table : tables) {
      DataObject dataObject = dataObjectRepository.findOne(ParetoAPI.schema.getId(), table.getName());

      for (Index index : table.getIndexes()) {
        String indexName = index.getName().equalsIgnoreCase("PRIMARY") ? "primary_key" : index.getName().toLowerCase();
        // Don't process foreign keys, as that is done in cardinality and plug-ins
        if (indexName.startsWith("fk_")) {
          continue;
        }
        DataIndex dataIndex = dataIndexRepository.findOne(dataObject.getId(), indexName);
        UUID rtIndexType = index.isUnique() ? uniqueIndex.getId() : nonUniqueIndex.getId();
        
        if (dataIndex == null) {
          DataIndexPostApiRequest request = new DataIndexPostApiRequest();
          request.setIdDataObject(dataObject.getId());
          request.setIdRtIndexType(rtIndexType);
          request.setName(indexName);
          request.setCreatedBy("RDBMS Import Post");
          dataIndexRepository.save(request); 

          dataIndex = dataIndexRepository.findOne(dataObject.getId(), indexName);
        }
        else {
          DataIndexPutApiRequest request = new DataIndexPutApiRequest();
          request.setId(dataIndex.getId());
          request.setIdRtIndexType(rtIndexType);
          request.setName(indexName);
          request.setUpdatedAt(dataIndex.getUpdatedAt());
          request.setUpdatedBy("RDBMS Import Put");
          dataIndexRepository.save(request); 
        }
        
        int sequence = 0;
        for (IndexColumn column : index.getColumns()) {
          String columnName = column.getName().toLowerCase();
          Property property = propertyRepository.findOne(dataObject.getId(), columnName);
          if (property == null) {
            throw new Exception ("Null Property on Index Column: " + columnName);
          }
          DataIndexProperty dataIndexProperty = dataIndexPropertyRepository.findOne(dataIndex.getId(), property.getId());
          String sortOrder = column.getSortSequence().name();
          UUID idSortOrder = null;
          
          if (sortOrder.equalsIgnoreCase(ascending.getName())) {
            idSortOrder = ascending.getId();
          }
          else {
            idSortOrder = descending.getId();
          }
            
          if (dataIndexProperty == null) {
            DataIndexPropertyPostApiRequest request = new DataIndexPropertyPostApiRequest();
            request.setIdDataIndex(dataIndex.getId());
            request.setIdProperty(property.getId());
            request.setIdRtSortOrder(idSortOrder);
            request.setSequence(++sequence);
            request.setCreatedBy("RDBMS Import Post");
            dataIndexPropertyRepository.save(request);
          }
          else {
            DataIndexPropertyPutApiRequest request = new DataIndexPropertyPutApiRequest();
            request.setId(dataIndexProperty.getId());
            request.setIdProperty(property.getId());
            request.setIdRtSortOrder(idSortOrder);
            request.setSequence(++sequence);
            request.setUpdatedAt(dataIndexProperty.getUpdatedAt());
            request.setUpdatedBy("RDBMS Import Put");
            dataIndexPropertyRepository.save(request);
          }         
        }      
      }
    }   
  }
  
  private static DatabaseConnectionSource getDataSource(String username, String password, String dbSchema) {
    final String connectionUrl = "jdbc:mysql://localhost:3306/" + dbSchema;
    return DatabaseConnectionSources.newDatabaseConnectionSource(
        connectionUrl, new MultiUseUserCredentials(username, password));
  }
}  