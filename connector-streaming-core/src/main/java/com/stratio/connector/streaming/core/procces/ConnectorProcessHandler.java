package com.stratio.connector.streaming.core.procces;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.connector.streaming.core.procces.exception.ConnectionProcessException;
import com.stratio.meta.common.exceptions.ExecutionException;

/**
 * Created by jmgomez on 3/10/14.
 */
public class ConnectorProcessHandler {

    /**
     * The log.
     */
    final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    private HashMap<String, ThreadProcess> processMap = new HashMap<>();


    public void strartProcess(String queryId, ConnectorProcess connectorProcess) throws ConnectionProcessException {
    if (processMap.containsKey(queryId)) {
        String msg = "The processMap with id " + queryId + " already exists ";
        logger.error(msg);
        throw new ConnectionProcessException(msg);
    }
        Thread thread = new Thread(connectorProcess,"[StreamingQuery-"+queryId+"]");
        processMap.put(queryId, new ThreadProcess(thread, connectorProcess));
        thread.start();
    }

    public ConnectorProcess getProcess(String queryId) throws ConnectionProcessException {
        if (!processMap.containsKey(queryId)) {
            String msg = "The processMap with id " + queryId + " not exists ";
            logger.error(msg);
            throw new ConnectionProcessException(msg);
        }
        return processMap.get(queryId).process;
    }

	public void stopProcess(String queryId) throws ConnectionProcessException, ExecutionException {
        ThreadProcess threadProcess = processMap.get(queryId);

            threadProcess.process.endQuery();
            threadProcess.thread.interrupt();
            processMap.remove(queryId);


		
	}

    private class ThreadProcess{

        Thread thread;
        ConnectorProcess process;

        ThreadProcess(Thread thread, ConnectorProcess process){
            this.thread = thread;
            this.process = process;
        }

    }

}
