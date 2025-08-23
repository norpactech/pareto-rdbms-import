package com.norpactech.pf.rdbms.service;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.pf.config.ConfiguredAPI;
import com.norpactech.pf.rdbms.dto.CardinalityPostApiRequest;
import com.norpactech.pf.rdbms.dto.CardinalityPutApiRequest;
import com.norpactech.pf.rdbms.dto.DataIndexPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataIndexPropertyPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataIndexPropertyPutApiRequest;
import com.norpactech.pf.rdbms.dto.DataIndexPutApiRequest;
import com.norpactech.pf.rdbms.dto.DataObjectDeleteApiRequest;
import com.norpactech.pf.rdbms.dto.DataObjectPostApiRequest;
import com.norpactech.pf.rdbms.dto.DataObjectPutApiRequest;
import com.norpactech.pf.rdbms.dto.PropertyDeleteApiRequest;
import com.norpactech.pf.rdbms.dto.PropertyPostApiRequest;
import com.norpactech.pf.rdbms.dto.PropertyPutApiRequest;
import com.norpactech.pf.rdbms.model.Cardinality;
import com.norpactech.pf.rdbms.model.Context;
import com.norpactech.pf.rdbms.model.ContextDataType;
import com.norpactech.pf.rdbms.model.DataIndex;
import com.norpactech.pf.rdbms.model.DataIndexProperty;
import com.norpactech.pf.rdbms.model.DataObject;
import com.norpactech.pf.rdbms.model.GenericDataTypeAttribute;
import com.norpactech.pf.rdbms.model.GenericPropertyType;
import com.norpactech.pf.rdbms.model.Property;
import com.norpactech.pf.rdbms.model.RefTableType;
import com.norpactech.pf.rdbms.model.RefTables;
import com.norpactech.pf.rdbms.model.Tenant;
import com.norpactech.pf.rdbms.model.Validation;
import com.norpactech.pf.rdbms.repository.CardinalityRepository;
import com.norpactech.pf.rdbms.repository.ContextDataTypeRepository;
import com.norpactech.pf.rdbms.repository.ContextRepository;
import com.norpactech.pf.rdbms.repository.DataIndexPropertyRepository;
import com.norpactech.pf.rdbms.repository.DataIndexRepository;
import com.norpactech.pf.rdbms.repository.DataObjectRepository;
import com.norpactech.pf.rdbms.repository.GenericDataTypeAttributeRepository;
import com.norpactech.pf.rdbms.repository.GenericPropertyTypeRepository;
import com.norpactech.pf.rdbms.repository.PropertyRepository;
import com.norpactech.pf.rdbms.repository.RefTableTypeRepository;
import com.norpactech.pf.rdbms.repository.RefTablesRepository;
import com.norpactech.pf.rdbms.repository.TenantRepository;
import com.norpactech.pf.rdbms.repository.ValidationRepository;
import com.norpactech.pf.rdbms.vo.ForeignKeyVO;
import com.norpactech.pf.utils.Constant;
import com.norpactech.pf.utils.TextUtils;

import schemacrawler.schema.Column;
import schemacrawler.schema.ColumnReference;
import schemacrawler.schema.ForeignKey;
import schemacrawler.schema.Index;
import schemacrawler.schema.IndexColumn;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.LimitOptionsBuilder;
import schemacrawler.schemacrawler.LoadOptionsBuilder;
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
  private static final TenantRepository tenantRepository = new TenantRepository();
  private static final GenericDataTypeAttributeRepository genericDataTypeAttributeRepository = new GenericDataTypeAttributeRepository();
  
  public static void importDatabase(String username, String password, String dbSchema) throws Exception {
    
    logger.info("Import Database Beginning with Schema: " + ConfiguredAPI.schema.getName());

    new LoggingConfig(Level.OFF);

    var limitOptionsBuilder =
      LimitOptionsBuilder.builder().includeSchemas("ub"::equals);

    var loadOptionsBuilder =
      LoadOptionsBuilder.builder().withSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
    
    var options =
      SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
        .withLimitOptions(limitOptionsBuilder.toOptions())
        .withLoadOptions(loadOptionsBuilder.toOptions());

    var catalog = SchemaCrawlerUtility.getCatalog(getDataSource(username, password, dbSchema), options);
    var catalogSchema = catalog.getSchemas().stream()
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
    var tables = catalog.getTables(catalogSchema);
    var dataObjects = dataObjectRepository.find(Map.of("idSchema", ConfiguredAPI.schema.getId())); 

    removeOrphanedDataObjects(tables, dataObjects);
    importTables(tables, paretoContext);    
    importCardinality(tables);    
    importIndexes(tables); 
    importIdentifyingRelationships(tables);
    logger.info("Import Database Completed with Schema: " + ConfiguredAPI.schema.getName());
  }
  /**
   * Remove any data objects whose associated table no longer exists
   * in the source database.
   */
  private static void removeOrphanedDataObjects(
      Collection<Table> tables, 
      Collection<DataObject> dataObjects) throws Exception {

    var deleteRequests = new ArrayList<DataObjectDeleteApiRequest>();
    
    for (var dataObject : dataObjects) {
      boolean exists = false;
      for (Table table : tables) {
        if (table.getName().equalsIgnoreCase(dataObject.getName())) {
          exists = true;
          break;
        }
      }
      if (!exists) {
        var request = new DataObjectDeleteApiRequest();
        request.setId(dataObject.getId());
        request.setUpdatedAt(dataObject.getUpdatedAt());
        request.setUpdatedBy("RDBMS Import Delete");
        deleteRequests.add(request);
      }
    }
    for (var request : deleteRequests) {
      var response = dataObjectRepository.delete(request);
      if (response.getData() != null) {
        logger.info("DataObject '{}' removed.", request.getId());
      }
      else {
        logger.warn("Ophaned Data Object '{}' was not removed! {}", request.getId(), response.getError());
      }
    }
  }
  
  private static void importTables(Collection<Table> tables, Context paretoContext) throws Exception {

    for (final Table table : tables) {
      DataObject dataObject = dataObjectRepository.findOne(ConfiguredAPI.schema.getId(), table.getName());

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
        request.setIdSchema(ConfiguredAPI.schema.getId());
        request.setName(table.getName());
        request.setDescription("Created " + table.getName() + " from Import");
        request.setHasIdentifier(hasIdentifier);
        request.setHasAudit(hasAudit);
        request.setHasActive(hasActive);
        request.setCreatedBy("RDBMS Import Post");
        dataObjectRepository.save(request);
        dataObject = dataObjectRepository.findOne(ConfiguredAPI.schema.getId(), table.getName());
      }
      else {
        var request = new DataObjectPutApiRequest();
        request.setId(dataObject.getId());
        request.setName(dataObject.getName());
        request.setDescription("Created " + table.getName() + " from Import");
        request.setHasIdentifier(hasIdentifier);
        request.setHasAudit(hasAudit);
        request.setHasActive(hasActive);
        request.setUpdatedAt(dataObject.getUpdatedAt());
        request.setUpdatedBy("RDBMS Import Put");
        dataObjectRepository.save(request);
      }
      
      
      removeOrphanedProperties(table, dataObject);
      importProperties(paretoContext, table);
      logger.info("DataObject '" + table.getName() + "' imported.");
    }
  }
  /**
   * Remove any properties whose associated table column no longer exists
   * in the source database.
   */
  private static void removeOrphanedProperties(
      Table table, 
      DataObject dataObject) throws Exception {

    var deleteRequests = new ArrayList<PropertyDeleteApiRequest>();
    var properties =  propertyRepository.find(Map.of("idDataObject", dataObject.getId()));

    for (var property : properties) {
      boolean exists = false;
      for (var column : table.getColumns()) {
        if (dataObject.getHasIdentifier() == true 
        && column.getName().equalsIgnoreCase("ID")) {
          continue;
        }
        else if (dataObject.getHasActive() == true 
        && column.getName().equalsIgnoreCase("IS_ACTIVE")) {
          continue;
        }
        else if (dataObject.getHasAudit() == true 
        && column.getName().equalsIgnoreCase("CREATED_AT")) {
          continue;
        }
        else if (dataObject.getHasAudit() == true 
          && column.getName().equalsIgnoreCase("CREATED_BY")) {
          continue;
        }
        else if (dataObject.getHasAudit() == true 
        && column.getName().equalsIgnoreCase("UPDATED_AT")) {
          continue;
        }
        else if (dataObject.getHasAudit() == true 
        && column.getName().equalsIgnoreCase("UPDATED_BY")) {
          continue;
        }
        if (column.getName().equalsIgnoreCase(property.getName())) {
          exists = true;
          break;
        }
      }
      if (!exists) {
        var request = new PropertyDeleteApiRequest();
        request.setId(property.getId());
        request.setUpdatedAt(property.getUpdatedAt());
        request.setUpdatedBy("RDBMS Import Delete");
        deleteRequests.add(request);
      }
    }
    for (var request : deleteRequests) {
      var response = propertyRepository.delete(request);
      if (response.getData() != null) {
        logger.info("Ophaned Property '" + request.getId() + "' removed.");
      }
      else {
        logger.warn("Ophaned Property '{}' was not removed! {}", request.getId(), response.getError());
      }
    }
  }
  
  private static void importProperties(Context context, Table table) throws Exception {
    
    DataObject dataObject = dataObjectRepository.findOne(ConfiguredAPI.schema.getId(), table.getName());
    List<ContextDataType> contextDataTypes = contextDataTypeRepository.find(Map.of("idContext", context.getId()));

    int i = 1;
    for (final Column column : table.getColumns()) {

      if (dataObject.getHasIdentifier() == true 
      && column.getName().equalsIgnoreCase("ID")) {
        continue;
      }
      else if (dataObject.getHasActive() == true 
      && column.getName().equalsIgnoreCase("IS_ACTIVE")) {
        continue;
      }
      else if (dataObject.getHasAudit() == true 
      && column.getName().equalsIgnoreCase("CREATED_AT")) {
        continue;
      }
      else if (dataObject.getHasAudit() == true 
      && column.getName().equalsIgnoreCase("CREATED_BY")) {
        continue;
      }
      else if (dataObject.getHasAudit() == true 
      && column.getName().equalsIgnoreCase("UPDATED_AT")) {
        continue;
      }
      else if (dataObject.getHasAudit() == true 
      && column.getName().equalsIgnoreCase("UPDATED_BY")) {
        continue;
      }      
      var contextDataType = getContextDataType(contextDataTypes, table, column);
      UUID idGenericDataType = contextDataType.getIdGenericDataType();
      // Validation
      Validation validation = validationRepository.findOne(ConfiguredAPI.tenant.getId(), column.getName());
      UUID idValidation = validation == null ? null : validation.getId();
      // Generic Property Type
      GenericPropertyType genericPropertyType = genericPropertyTypeRepository.findOne(idGenericDataType, column.getName());
      UUID idGenericPropertyType = genericPropertyType == null ? null : genericPropertyType.getId();
      
      Property property = propertyRepository.findOne(dataObject.getId(), column.getName());
      
      var genericDataTypeAttributes = genericDataTypeAttributeRepository.find(Map.of("idGenericDataType", idGenericDataType));
      Integer length = null;
      Integer digits = null;
      
      for (GenericDataTypeAttribute genericDataTypeAttribute : genericDataTypeAttributes) {
        if (genericDataTypeAttribute.getName().equalsIgnoreCase("length")) {
          length = column.getSize();
        }
        if (genericDataTypeAttribute.getName().equalsIgnoreCase("precision")) {
          digits = column.getDecimalDigits();
        }
      }
      /*
       * Sometimes true/false is expressed by 0/1 (false/true) - i.e. MySQL.
       * ... override this behavior.
       */
      String defaultValue = null;
      if (contextDataType.getAlias().equalsIgnoreCase("boolean")
      &&  StringUtils.isNotEmpty(column.getDefaultValue())) {
        
        if (column.getDefaultValue().equals("0")) {
          defaultValue = "FALSE";
        }
        else if (column.getDefaultValue().equals("1")) {
          defaultValue = "TRUE";
        }
        else {
          defaultValue = column.getDefaultValue(); 
        }
      }
      else {
        defaultValue = column.getDefaultValue(); 
      }
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
        request.setLength(length);
        request.setScale(digits);
        request.setIsNullable(column.isNullable());
        request.setDefaultValue(defaultValue);
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
        request.setIsUpdatable(true);
        request.setFkViewable(property.getFkViewable());
        request.setLength(length);
        request.setScale(digits);
        request.setIsNullable(column.isNullable());
        request.setDefaultValue(defaultValue);
        request.setUpdatedAt(property.getUpdatedAt());
        request.setUpdatedBy("RDBMS Import Put");
        propertyRepository.save(request);
      }
    }
  }

  private static ContextDataType getContextDataType(
      List<ContextDataType> contextDataTypes, 
      Table table, 
      Column column) throws Exception {
    
    String columnDataType = column.getColumnDataType().getName();
    if (StringUtils.isNotEmpty(column.getWidth())) {
      /*
       *  TODO: Char(36) is a special data type for UUIDs. This needs to 
       *  be a documented anomaly. Doubtful if someone would use char(36)
       *  otherwise.  
       */
      if (columnDataType.equalsIgnoreCase("char")
      &&  column.getWidth().equalsIgnoreCase("(36)")) { 
        columnDataType += column.getWidth();
      }
    }
    
    for (ContextDataType contextDataType : contextDataTypes) {
      if (columnDataType.equalsIgnoreCase(contextDataType.getName())) {
        return contextDataType;
      }
    }
    throw new Exception("Unsupported Context Data Type! '" + columnDataType + ", " + table.getName() + " - " + column.getName() + "' Terminating...");
  }
  
  private static void importCardinality(Collection<Table> tables) throws Exception {
    
    Tenant tenant = tenantRepository.findOne(Constant.SYSTEM_TENANT);
    if (tenant == null) {
      throw new Exception ("System Tenant " + Constant.SYSTEM_TENANT + " not found! Terminating...");
    }

    RefTableType cardinalityTT = refTableTypeRepository.findOne(tenant.getId(), Constant.CARDINALITY_TABLE_TYPE);
    if (cardinalityTT == null) {
      throw new Exception ("Cardinality Table Type not found! Terminating...");
    }
    RefTables oneToMany = refTablesRepository.findOne(cardinalityTT.getId(), Constant.ONE_TO_MANY);
    if (oneToMany == null) {
      throw new Exception ("M:1 Cardinality not found! Terminating...");
    }
    RefTableType cardinalityStrengthTT = refTableTypeRepository.findOne(tenant.getId(), Constant.CARDINALITY_STRENGTH_TABLE_TYPE); 
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

      DataObject tableDataObject = dataObjectRepository.findOne(ConfiguredAPI.schema.getId(), fk.getTable());
      if (tableDataObject == null) {
        throw new Exception ("Foreign Key Table Data Object '" + fk.getTable() + "' not Found! Terminating...");
      }
      Property foreignKeyProperty = propertyRepository.findOne(tableDataObject.getId(), fk.getForeignKey());
      if (foreignKeyProperty == null) {
        throw new Exception ("Foreign Key Property '" + fk.getForeignKey() + "' not Found! Terminating...");
      }
      DataObject referencesDataObject = dataObjectRepository.findOne(ConfiguredAPI.schema.getId(), fk.getReferences());
      if (referencesDataObject == null) {
        throw new Exception ("Foreign Key References Data Object '" + fk.getReferences() + "' not Found! Terminating...");
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
        postRequest.setHasReferentialAction(true);
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
        putRequest.setHasReferentialAction(cardinality.getHasReferentialAction());
        putRequest.setUpdatedAt(cardinality.getUpdatedAt());
        putRequest.setUpdatedBy("RDBMS Import Put");
        
        cardinalityRepository.save(putRequest);
      }
    }
  }
  
  private static void importIndexes(Collection<Table> tables) throws Exception {

    Tenant tenant = tenantRepository.findOne(Constant.SYSTEM_TENANT);
    if (tenant == null) {
      throw new Exception ("System Tenant " + Constant.SYSTEM_TENANT + " not found! Terminating...");
    }

    RefTableType indexType = refTableTypeRepository.findOne(tenant.getId(), Constant.INDEX_TYPE); 
    if (indexType == null) {
      throw new Exception ("Index Type Table Type not found! Terminating...");
    }
    RefTables uniqueIndex = refTablesRepository.findOne(indexType.getId(), Constant.UNIQUE_INDEX);
    RefTables nonUniqueIndex = refTablesRepository.findOne(indexType.getId(), Constant.INDEX);

    RefTableType rtSortOrder = refTableTypeRepository.findOne(tenant.getId(), Constant.SORT_ORDER); 
    if (rtSortOrder == null) {
      throw new Exception ("Sort Order Table Type not found! Terminating...");
    }
    RefTables ascending = refTablesRepository.findOne(rtSortOrder.getId(), Constant.ASCENDING);
    RefTables descending = refTablesRepository.findOne(rtSortOrder.getId(), Constant.DESCENDING);
    
    for (Table table : tables) {
      DataObject dataObject = dataObjectRepository.findOne(ConfiguredAPI.schema.getId(), table.getName());

      for (Index index : table.getIndexes()) {
        String indexName = index.getName().equalsIgnoreCase("PRIMARY") ? "primary_key" : index.getName().toLowerCase();
        if (indexName.equals("primary_key")
        &&  dataObject.getHasIdentifier()) {
          continue;
        }
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
  /**
   * Identifying foreign key values (parent/child) should not be updated in the child as a rule.
   * exceptions, such as a data fix, should be manually handled.
   */
  private static void importIdentifyingRelationships(Collection<Table> tables) throws Exception {
    
    logger.info("Analyzing identifying relationships...");

    for (var table : tables) {      
      for (var foreignKey : table.getForeignKeys()) {
        var tableName = TextUtils.lastDelimetedValue(foreignKey.getDependentTable().getFullName(), ".");
        var primaryKey = table.getPrimaryKey();
        if (primaryKey == null) {
          continue;
        }
        for (ColumnReference reference : foreignKey.getColumnReferences()) {
          var foreignKeyColumn = reference.getForeignKeyColumn();
          var foreignKeyName = TextUtils.lastDelimetedValue(foreignKeyColumn.getName(), ".");
          boolean isIdentifying = false;

          for (var tableColumn : table.getColumns()) {
            if (tableColumn.getName().equals(foreignKeyColumn.getName()) && 
                tableColumn.isPartOfPrimaryKey()) {
              isIdentifying = true;
              break;
            }
          }
          
          if (isIdentifying) {
            logger.debug("Found identifying relationship: Table '{}', FK Column '{}'", tableName, foreignKeyName);
            
            // Find the DataObject and Property
            DataObject dataObject = dataObjectRepository.findOne(ConfiguredAPI.schema.getId(), tableName.replace("`", ""));
            if (dataObject == null) {
              logger.warn("DataObject not found for table: {}", tableName);
              continue;
            }
            
            Property property = propertyRepository.findOne(dataObject.getId(), foreignKeyName.replace("`", ""));
            if (property == null) {
              logger.warn("Property not found for FK column: {} in table: {}", foreignKeyName, tableName);
              continue;
            }
            
            // Set the property as non-updatable since it's part of an identifying relationship
            var request = new PropertyPutApiRequest();
            request.setId(property.getId());
            request.setIdGenericDataType(property.getIdGenericDataType());
            request.setIdValidation(property.getIdValidation());
            request.setIdGenericPropertyType(property.getIdGenericPropertyType());
            request.setSequence(property.getSequence());
            request.setName(property.getName());
            request.setDescription(property.getDescription());
            request.setIsUpdatable(false);
            request.setFkViewable(property.getFkViewable());
            request.setLength(property.getLength());
            request.setScale(property.getScale());
            request.setIsNullable(property.getIsNullable());
            request.setDefaultValue(property.getDefaultValue());
            request.setUpdatedAt(property.getUpdatedAt());
            request.setUpdatedBy("RDBMS Identifying");
            
            var response = propertyRepository.save(request);
            if (response.getData() != null) {
              logger.debug("Property '{}' in table '{}' set as non-updatable (identifying relationship)",  property.getName(), tableName);
            }
            else {
              logger.error("Property '{}' in table '{}' update failed with (identifying relationship)",  property.getName(), tableName, response.getError());
            }
          }
        }
      }
    }
    logger.info("Identifying relationships analysis completed.");
  }
  
  private static DatabaseConnectionSource getDataSource(String username, String password, String dbSchema) {
    final String connectionUrl = "jdbc:mysql://localhost:3306/" + dbSchema;
    return DatabaseConnectionSources.newDatabaseConnectionSource(
        connectionUrl, new MultiUseUserCredentials(username, password));
  }
}
