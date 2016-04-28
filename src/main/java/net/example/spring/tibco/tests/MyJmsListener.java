package net.example.spring.tibco.tests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 */
@Component
public class MyJmsListener implements MessageListener {

    private static final Log logger = LogFactory.getLog(MyJmsListener.class);

    public void onMessage(Message message) {
        try {
//            int messageCount = message.getIntProperty(JmsMessageProducer.MESSAGE_COUNT);
            logger.info("Trying to get a message");

            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage)message;
                String msg = tm.getText();


                logger.info("Received message: " + msg);
//                logger.info("Processed message '{}'.  value={}", msg, messageCount);

//                counter.incrementAndGet();
            }
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
