package com.journalize.journalize.messaging.kafka;

import com.journalize.journalize.services.EmailService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.journalize.journalize.constants.Constants;
import com.journalize.journalize.dto.user.SentimentData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = Constants.KAFKA_ENABLED, havingValue = "true", matchIfMissing = true)
public class KafkaProducerService {

    private final EmailService emailService;
    private final KafkaTemplate<String, SentimentData> kafkaTemplate;

    public void send(String key, SentimentData sentimentData) {
        CompletableFuture<SendResult<String, SentimentData>> future = kafkaTemplate
                .send(Constants.KAFKA_TOPIC_WEEKLY_SENTIMENT, key, sentimentData);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Kafka Message failed to send message with key [{}]: {}", key, ex.getMessage());
                var messageId = emailService.sendEmail(
                        sentimentData.getEmail(),
                        sentimentData.getSubject(),
                        sentimentData.getBody());

                log.info(
                        "Kafka message failed to sent with customer email: {} used backup email sent with messageId: {}",
                        sentimentData.getEmail(), messageId);
            } else {
                log.info("Kafka message sent with key [{}]: {}", key, result.getProducerRecord().value().getEmail());
            }
        });
    }
}