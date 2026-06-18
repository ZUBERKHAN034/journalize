package com.journalize.journalize.kafka;

import com.journalize.journalize.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.journalize.journalize.constants.Constants;
import com.journalize.journalize.dto.user.SentimentData;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final EmailService emailService;

    @KafkaListener(topics = Constants.KAFKA_TOPIC_WEEKLY_SENTIMENT, groupId = Constants.KAFKA_GROUP_JOURNALIZE)
    public void consume(SentimentData sentimentData) {
        var messageId = emailService.sendEmail(sentimentData.getEmail(), sentimentData.getSubject(),
                sentimentData.getBody());
        log.info("Kafka message consumed with data: {} and email sent with messageId: {}", sentimentData, messageId);
    }
}