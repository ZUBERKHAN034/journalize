package com.journalize.journalize.messaging.rabbitmq;

import com.journalize.journalize.constants.Constants;
import com.journalize.journalize.dto.user.SentimentData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Value(Constants.RABBITMQ_EXCHANGE)
    private String exchange;

    @Value(Constants.RABBITMQ_ROUTING_KEY)
    private String routingKey;

    public void send(SentimentData sentimentData) {

        rabbitTemplate.convertAndSend(exchange, routingKey, sentimentData);

        log.info("RabbitMQ message sent with key [{}]: {}", routingKey, sentimentData.getEmail());
    }
}