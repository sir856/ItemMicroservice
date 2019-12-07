package com.developing.shop.items.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("localhost");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        return admin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public ConditionalRejectingErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(new MyFatalExceptionStrategy());
    }

    @Bean
    public Queue addItemQueue() {
        return new Queue("addItemToOrder");
    }

    @Bean
    public Queue deleteItemQueue() {
        return new Queue("deleteItemFromOrder");
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("itemExchange");
    }

    @Bean
    public Binding bindingAddItemMessage(){
        return BindingBuilder.bind(addItemQueue()).to(directExchange()).with("add");
    }

    @Bean
    public Binding bindingDeleteItemMessage(){
        return BindingBuilder.bind(deleteItemQueue()).to(directExchange()).with("delete");
    }

}
