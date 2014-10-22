package com.stratio.connector.streaming.core.engine.query.queryexecutor.messageProcess;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stratio.connector.streaming.core.engine.query.ConnectorQueryData;
import com.stratio.connector.streaming.core.engine.query.queryexecutor.messageprocess.ElementNumberProcessMessage;
import com.stratio.connector.streaming.core.engine.query.queryexecutor.messageprocess.ProcessMessageFactory;
import com.stratio.connector.streaming.core.engine.query.queryexecutor.messageprocess.TimeWindowProcessMessage;
import com.stratio.connector.streaming.core.engine.query.util.ResultsetCreator;
import com.stratio.crossdata.common.connector.Operations;
import com.stratio.crossdata.common.logicalplan.Window;
import com.stratio.crossdata.common.statements.structures.window.TimeUnit;
import com.stratio.crossdata.common.statements.structures.window.WindowType;

/**
 * ProccesMessageFactory Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>
 * oct 16, 2014
 * </pre>
 */
@RunWith(PowerMockRunner.class)
public class ProccesMessageFactoryTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getProccesMessage(ConnectorQueryData queryData, ResultsetCreator resultSetCreator)
     */
    @Test
    public void testGetProccesMessage() throws Exception {

        ConnectorQueryData queryDataNum = new ConnectorQueryData();
        queryDataNum.setWindow(new Window(Operations.SELECT_WINDOW, WindowType.NUM_ROWS));
        assertTrue("number row is correct",
                        ProcessMessageFactory.getProccesMessage(queryDataNum, mock(ResultsetCreator.class)) instanceof ElementNumberProcessMessage);

        ConnectorQueryData queryDataTemporal = new ConnectorQueryData();
        Window window = new Window(Operations.SELECT_WINDOW, WindowType.TEMPORAL);
        window.setTimeWindow(10, TimeUnit.DAYS);
        queryDataTemporal.setWindow(window);
        assertTrue("number row is correct",
                        ProcessMessageFactory.getProccesMessage(queryDataTemporal, mock(ResultsetCreator.class)) instanceof TimeWindowProcessMessage);

    }

}
