/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ml.littlebulb.presto.kudu;

import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ConnectorSession;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.RecordSet;
import com.facebook.presto.spi.connector.ConnectorRecordSetProvider;
import com.facebook.presto.spi.connector.ConnectorTransactionHandle;

import javax.inject.Inject;
import java.util.List;

import static ml.littlebulb.presto.kudu.Types.checkType;
import static java.util.Objects.requireNonNull;

public class KuduRecordSetProvider implements ConnectorRecordSetProvider {

    private final String connectorId;
    private final KuduClientSession clientSession;

    @Inject
    public KuduRecordSetProvider(KuduConnectorId connectorId, KuduClientSession clientSession) {
        this.connectorId = requireNonNull(connectorId, "connectorId is null").toString();
        this.clientSession = clientSession;
    }

    @Override
    public RecordSet getRecordSet(ConnectorTransactionHandle transactionHandle,
                                  ConnectorSession session, ConnectorSplit split, List<? extends ColumnHandle> columns) {
        requireNonNull(split, "split is null");
        requireNonNull(columns, "columns is null");

        KuduSplit kuduSplit = checkType(split, KuduSplit.class, "split is not class KuduSplit");

        return new KuduRecordSet(clientSession, kuduSplit, columns);
    }

    public KuduClientSession getClientSession() {
        return clientSession;
    }
}
