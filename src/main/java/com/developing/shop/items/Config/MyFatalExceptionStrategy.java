package com.developing.shop.items.Config;

import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;

public class MyFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

    @Override
    protected boolean isUserCauseFatal(Throwable cause) {
        return cause instanceof IllegalArgumentException;
    }
}
