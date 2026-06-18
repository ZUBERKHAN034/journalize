package com.journalize.journalize.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.journalize.journalize.constants.Constants;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Bean
    public NewTopic journalizeEventsTopic() {
        return TopicBuilder.name(Constants.KAFKA_TOPIC_WEEKLY_SENTIMENT)
                .partitions(1)
                .replicas(1)
                .build();
    }
}