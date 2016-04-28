import net.example.spring.tibco.tests.JmsLogger;
import net.example.spring.tibco.tests.MyJmsListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.Message;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class JmsTester {

    private static final Log logger = LogFactory.getLog(MyJmsListener.class);

    @Autowired
    JmsLogger jmsLogger;

    @Value("#{myProps['jms.response.queue']}")
    private String jmsResponseQueue;

    @Test
    public void sendAMessage() throws Exception {
        assertNotNull(jmsLogger);
        assertNotNull(jmsResponseQueue);

        JmsLogger.log(String.class, "Whatt!!!?", null);

        Message message = JmsLogger.JMSReceive(jmsResponseQueue);
        assertNotNull("Did not find response message", message);
        logger.debug("Received message: " + message);
    }
}
