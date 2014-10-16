/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 *  See the NOTICE file distributed with this work for additional information
 *  regarding copyright ownership. The STRATIO (C) licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package com.stratio.connector.streaming.ftest.functionalInsert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.stratio.connector.commons.ftest.functionalInsert.GenericBulkInsertTest;
import com.stratio.connector.commons.ftest.helper.IConnectorHelper;
import com.stratio.connector.commons.ftest.schema.TableMetadataBuilder;
import com.stratio.connector.streaming.core.StreamingConnector;
import com.stratio.connector.streaming.ftest.helper.StreamingConnectorHelper;
import com.stratio.meta.common.connector.ConnectorClusterConfig;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.exceptions.ConnectionException;
import com.stratio.meta.common.exceptions.ExecutionException;
import com.stratio.meta.common.exceptions.InitializationException;
import com.stratio.meta.common.exceptions.UnsupportedException;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.metadata.TableMetadata;

/**
 * Created by jmgomez on 4/09/14.
 */
public class StreamingBulkInsertTest extends GenericBulkInsertTest{


    @Override
    protected IConnectorHelper getConnectorHelper() {
        StreamingConnectorHelper streamingConnectorHelper = null;
        try {
            streamingConnectorHelper = new StreamingConnectorHelper(getClusterName());
        } catch (InitializationException e) {
            e.printStackTrace();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return streamingConnectorHelper;
    }
}
