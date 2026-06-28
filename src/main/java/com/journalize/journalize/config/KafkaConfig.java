package com.journalize.journalize.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.journalize.journalize.constants.Constants;

@Configuration
@ConditionalOnProperty(name = Constants.KAFKA_ENABLED, havingValue = "true", matchIfMissing = true)
public class KafkaConfig {

    @Bean
    public NewTopic journalizeEventsTopic() {
        return TopicBuilder.name(Constants.KAFKA_TOPIC_WEEKLY_SENTIMENT)
                .partitions(1)
                .replicas(1)
                .build();
    }
}