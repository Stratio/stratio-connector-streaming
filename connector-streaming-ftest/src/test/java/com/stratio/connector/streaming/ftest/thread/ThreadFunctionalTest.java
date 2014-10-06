package com.stratio.connector.streaming.ftest.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.stratio.connector.commons.ftest.workFlow.LogicalWorkFlowCreator;
import com.stratio.connector.streaming.core.StreamingConnector;
import com.stratio.connector.streaming.ftest.thread.actions.StreamingInserter;
import com.stratio.connector.streaming.ftest.thread.actions.StreamingRead;
import com.stratio.meta.common.connector.ConnectorClusterConfig;
import com.stratio.meta.common.connector.IResultHandler;
import com.stratio.meta.common.connector.Operations;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.exceptions.ConnectionException;
import com.stratio.meta.common.exceptions.ExecutionException;
import com.stratio.meta.common.exceptions.UnsupportedException;
import com.stratio.meta.common.logicalplan.Filter;
import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.logicalplan.Project;
import com.stratio.meta.common.logicalplan.Select;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.statements.structures.relationships.Operator;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;

import static org.junit.Assert.*;

public class ThreadFunctionalTest {
	
	
	private static final String OTHER_TEXT = "OTHER...... ";
	private static final int WAIT_TIME = 30000;
	private static final String CATALOG_NAME = "catalog_name";
	private static final String TABLE_NAME = "table_name";
	String ZOOKEEPER_SERVER = "192.168.0.2"; //"10.200.0.58";// 
    String KAFKA_SERVER =  "192.168.0.2"; //"10.200.0.58"; //
    String KAFKA_PORT = "9092";
    String ZOOKEEPER_PORT = "2181";
    public static String STRING_COLUMN ="string_column";
    public static String INTEGER_COLUMN= "integer_column";
    public static String BOOLEAN_COLUMN = "boolean_column";
    
    StreamingConnector sC;
    TableMetadata tableMetadata;
    
    ClusterName clusterName = new ClusterName("CLUSTERNAME");
    
	@Before
	public void setUp() throws ConnectionException, UnsupportedException, ExecutionException{
		 sC = new StreamingConnector();
		 
		 sC.init(null);

	        Map<String, String> optionsNode = new HashMap<>();

	        optionsNode.put("KafkaServer", KAFKA_SERVER);
	        optionsNode.put("KafkaPort", KAFKA_PORT);
	        optionsNode.put("zooKeeperServer", ZOOKEEPER_SERVER);
	        optionsNode.put("zooKeeperPort", ZOOKEEPER_PORT);
	        
	        

	        ConnectorClusterConfig config = new ConnectorClusterConfig(clusterName, optionsNode);
	        sC.connect(null, config);
	        TableName tableName = new TableName(CATALOG_NAME, TABLE_NAME);
	        Map<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
	        ColumnName columnNameString =new ColumnName(tableName,STRING_COLUMN );
	        columns.put(columnNameString, new ColumnMetadata(columnNameString, new Object[]{}, ColumnType.VARCHAR));
	        ColumnName columnNameInteger =new ColumnName(tableName,INTEGER_COLUMN );
	        columns.put(columnNameInteger, new ColumnMetadata(columnNameInteger, new Object[]{}, ColumnType.INT));
	        ColumnName columnNameBoolean =new ColumnName(tableName,BOOLEAN_COLUMN );
	        columns.put(columnNameBoolean, new ColumnMetadata(columnNameBoolean, new Object[]{}, ColumnType.BOOLEAN));
	        tableMetadata = new TableMetadata(tableName, Collections.EMPTY_MAP, columns, Collections.EMPTY_MAP, clusterName, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
			
	        try{
	        	sC.getMetadataEngine().createTable(clusterName, tableMetadata );
	        }catch(ExecutionException t){
	        	
	        }
	}
	
	
	@Test
	public void testStopReadBeforeStopWrite() throws InterruptedException{
		
	
		StreamingInserter stramingInserter = new StreamingInserter(sC, clusterName, tableMetadata);
		stramingInserter.start();
		

		LogicalWorkflow logicalWokflow = new LogicalWorkFlowCreator(CATALOG_NAME, TABLE_NAME, clusterName).addColumnName(STRING_COLUMN).addColumnName(INTEGER_COLUMN).addColumnName(BOOLEAN_COLUMN).getLogicalWorkflow();
		
		StreamingRead stremingRead = new StreamingRead(sC, clusterName, tableMetadata,logicalWokflow, new ResultHandler());
		
		stremingRead.start();
		System.out.println("********************** Quering......");
		Thread.currentThread().sleep(WAIT_TIME);
		

		stremingRead.end();
		System.out.println("********************** END Quering......");
		Thread.currentThread().sleep(WAIT_TIME);
		System.out.println("********************** Change Test Quering......");
		stramingInserter.changeOtuput(OTHER_TEXT);
		Thread.currentThread().sleep(WAIT_TIME);
		
		System.out.println("********************** END Insert......");
		stramingInserter.end();
		Thread.currentThread().sleep(WAIT_TIME);
        
		
	}
	
	
	@Test
	public void testStopWriteBeforeStopRead() throws InterruptedException{
		
	
		StreamingInserter stramingInserter = new StreamingInserter(sC, clusterName, tableMetadata);
		stramingInserter.start();
		

		LogicalWorkflow logicalWokflow = new LogicalWorkFlowCreator(CATALOG_NAME, TABLE_NAME, clusterName).addColumnName(STRING_COLUMN).addColumnName(INTEGER_COLUMN).addColumnName(BOOLEAN_COLUMN).getLogicalWorkflow();
		
		ResultHandler resultHandler = new ResultHandler();
		StreamingRead stremingRead = new StreamingRead(sC, clusterName, tableMetadata,logicalWokflow, resultHandler);
		
		stremingRead.start();
		System.out.println("********************** Quering......");
		Thread.currentThread().sleep(WAIT_TIME);
		
		System.out.println("********************** END Insert......");
		stramingInserter.end();
		Thread.currentThread().sleep(10000); //it must be at least bigger than the windows time
		resultHandler.mustNotReadMore();
		System.out.println("********************** Wait for stoped read......");
		Thread.currentThread().sleep(2*WAIT_TIME);
		
		stremingRead.end();
		System.out.println("********************** END Quering......");
		Thread.currentThread().sleep(WAIT_TIME);
        
		
	}
	
	private class ResultHandler implements IResultHandler{

		boolean mustRead = true;
		@Override
		public void processException(String queryId,
				ExecutionException exception) {
			System.out.println(queryId + " "+exception.getMessage());
			exception.printStackTrace();
			
		}

		public void mustNotReadMore() {
			mustRead = false;
			
		}

		@Override
		public void processResult(QueryResult result) {
			assertTrue("Now it must read",mustRead);
			for (Row row: result.getResultSet()){
				Cell cell = row.getCell(STRING_COLUMN);
				if (cell!=null){
					Object value = cell.getValue();
					assertNotEquals("The result must not be different", OTHER_TEXT,value);
				}
			}
			
		}
		
	}

}
