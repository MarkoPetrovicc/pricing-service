package com.example.messageserver.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service(value = "priceErrorHandler")
public class PriceErrorHandler implements KafkaListenerErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PriceErrorHandler.class);

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        LOG.warn("Exception", message.getPayload(),
                exception.getMessage());

        if(exception.getCause() instanceof RuntimeException){
            throw exception;
        }
        return null;
    }
}
