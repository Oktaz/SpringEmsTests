package net.example.spring.tibco.tests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.Message;
import javax.jms.TextMessage;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class JmsRequestResponseTest {

    private static final Log logger = LogFactory.getLog(JmsRequestResponseTest.class);

    @Autowired
    JmsLogger jmsLogger;

    @Value("#{myProps['jms.response.queue']}")
    private String jmsResponseQueue;

    @Value("#{myProps['jms.request.queue']}")
    private String jmsRequestQueue;

    @Before
    public void clearOutQueues() throws Exception {

        logger.debug("Clearing out queues...");
        jmsLogger.getJmsTemplate().setReceiveTimeout(1);
        Message message = null;
        do {
            message = JmsLogger.JMSReceive(jmsResponseQueue);
        } while (message != null);

        do {
            message = JmsLogger.JMSReceive(jmsRequestQueue);
        } while (message != null);

        jmsLogger.getJmsTemplate().setReceiveTimeout(3000);
    }

    @Test
    public void sendAMessage() throws Exception {
        assertNotNull(jmsLogger);
        assertNotNull(jmsResponseQueue);

        final String messageToSend = "Hello, World";
        JmsLogger.log(String.class, messageToSend, null);

        Message message = JmsLogger.JMSReceive(jmsResponseQueue);
        assertNotNull("Did not find response message", message);
        logger.debug("Received message: " + message);
        assertTrue(message instanceof TextMessage);
        assertEquals("", "You sent this message: " + messageToSend, ((TextMessage)message).getText());
    }
}
