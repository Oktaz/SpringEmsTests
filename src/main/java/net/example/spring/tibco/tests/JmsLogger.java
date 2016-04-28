package net.example.spring.tibco.tests;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;


@Component
public class JmsLogger {

    private final static Logger logger = Logger
            .getLogger(Logger.class);

    private static JmsTemplate jmsTemplate;


    public JmsLogger() {}

    /**
     * @param <T>
     *
     */
    public static <T> void log(Class<T> clazz, Object message, Throwable t) {

        try {

            JMSSend("INT.JMSTEST.REQ", clazz, message, t);
            logger.debug("Message Sent...");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * @param Queue
     * @param clazz
     * @param message
     * @param t
     * @Description It uses org.springframework.jms.core.JmsTemplate to send JMS message to tibco Queue.
     * @author Parthasarathy Balakrishnan
     */
    static <T> void JMSSend(String Queue, final Class<T> clazz, final Object message,
                            final Throwable t) {
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage sendMessage = session
                        .createTextMessage((String) message);
                return sendMessage;
            }
        });
    }

    public static Message JMSReceive(String queue) {

        jmsTemplate.setReceiveTimeout(3000);
        Message receive = jmsTemplate.receive(queue);
        return receive;
    }

    /**
     * @return
     */
    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    /**
     * @param jmsTemplate
     */
    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        JmsLogger.jmsTemplate = jmsTemplate;
    }

}