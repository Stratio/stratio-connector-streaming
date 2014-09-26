/*
 * Stratio Meta
 *
 *   Copyright (c) 2014, Stratio, All rights reserved.
 *
 *   This library is free software; you can redistribute it and/or modify it under the terms of the
 *   GNU Lesser General Public License as published by the Free Software Foundation; either version
 *   3.0 of the License, or (at your option) any later version.
 *
 *   This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *   even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License along with this library.
 */
package com.stratio.connector.streaming.core.engine;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.connector.commons.connection.Connection;
import com.stratio.connector.commons.engine.CommonsStorageEngine;
import com.stratio.connector.streaming.core.connection.StreamingConnectionHandler;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.exceptions.ExecutionException;
import com.stratio.meta.common.exceptions.UnsupportedException;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.streaming.api.IStratioStreamingAPI;

/**
 * This class performs operations insert and delete in Elasticsearch.
 */

public class StreamingStorageEngine extends CommonsStorageEngine<IStratioStreamingAPI> {

    /**
     * The Log.
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());
 
    /**
     * Constructor.
     *
     * @param connectionHandler the connection handler.
     */
    public StreamingStorageEngine(StreamingConnectionHandler  connectionHandler) {

        super(connectionHandler);
    }

    /**
     * Insert a document in Elasticsearch.
     *
     *
     * @param targetTable   the targetName.
     * @param row           the row.
     * @throws com.stratio.meta.common.exceptions.ExecutionException   in case of failure during the execution.
     * @throws com.stratio.meta.common.exceptions.UnsupportedException it the operation is not supported.
     */

    @Override
    protected void insert(TableMetadata targetTable, Row row, Connection<IStratioStreamingAPI> connection)
            throws UnsupportedException, ExecutionException {

    	 throw new UnsupportedException("insert not supported in Streaming connector");
    }

    /**
     * Insert a set of documents in Elasticsearch.
     *
     *
     * @param rows        the set of rows.
     * @throws com.stratio.meta.common.exceptions.ExecutionException   in case of failure during the execution.
     * @throws com.stratio.meta.common.exceptions.UnsupportedException if the operation is not supported.
     */
    @Override
    protected void insert( TableMetadata targetTable, Collection<Row> rows,
            Connection<IStratioStreamingAPI> connection) throws UnsupportedException, ExecutionException {

    	 throw new UnsupportedException("insert bulk not supported in Streaming connector");
    }

   

}



