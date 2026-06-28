package com.journalize.journalize.messaging.rabbitmq;

import com.journalize.journalize.constants.Constants;
import com.journalize.journalize.dto.user.SentimentData;
import com.journalize.journalize.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumerService {

    private final EmailService emailService;

    @RabbitListener(queues = Constants.RABBITMQ_QUEUE)
    public void consume(SentimentData sentimentData) {
        var messageId = emailService.sendEmail(sentimentData.getEmail(), sentimentData.getSubject(),
                sentimentData.getBody());

        log.info("RabbitMQ message consumed with customer email: {} and email sent with messageId: {}",
                sentimentData.getEmail(), messageId);
    }
}