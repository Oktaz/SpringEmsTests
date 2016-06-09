package net.example.spring.tibco.tests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.Message;
import javax.jms.TextMessage;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class JmsRequestResponseTestIT {

    private static final Log logger = LogFactory.getLog(JmsRequestResponseTestIT.class);

    @Autowired
    JmsMessenger jmsMessenger;

    @Value("#{myProps['jms.response.queue']}")
    private String jmsResponseQueue;

    @Value("#{myProps['jms.request.queue']}")
    private String jmsRequestQueue;

    @Before
    public void clearOutQueues() throws Exception {

        logger.debug("Clearing out queues...");
        jmsMessenger.getJmsTemplate().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        Message message = null;
        do {
            message = JmsMessenger.JMSReceive(jmsResponseQueue);
        } while (message != null);

        do {
            message = JmsMessenger.JMSReceive(jmsRequestQueue);
        } while (message != null);

        jmsMessenger.getJmsTemplate().setReceiveTimeout(3000);
    }

    @Test
    public void sendAMessage() throws Exception {
        assertNotNull(jmsMessenger);
        assertNotNull(jmsResponseQueue);

        final String messageToSend = "Hello, World";
        JmsMessenger.log(String.class, messageToSend, null);

        Message message = JmsMessenger.JMSReceive(jmsResponseQueue);
        assertNotNull("Did not find response message", message);
        logger.debug("Received message: " + message);
        assertTrue(message instanceof TextMessage);
        assertEquals("", "You sent this message: " + messageToSend, ((TextMessage)message).getText());
    }
}
