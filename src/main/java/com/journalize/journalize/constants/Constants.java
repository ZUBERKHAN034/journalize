package com.journalize.journalize.constants;

public class Constants {
    public static final String CITY = "<CITY>";
    public static final String API_KEY = "<API_KEY>";
    public static final String APP_CACHE_KEY_WEATHERSTACK_API_URL = "WEATHERSTACK_API_URL";
    public static final String TEMPLATE_EMAIL_WEEKLY_SENTIMENT = "email-weekly-sentiment";
    public static final String KAFKA_TOPIC_WEEKLY_SENTIMENT = "weekly-sentiment";
    public static final String KAFKA_GROUP_JOURNALIZE = "${spring.kafka.consumer.group-id}";
    public static final String KAFKA_ENABLED = "kafka.enabled";
    public static final String RABBITMQ_QUEUE = "${rabbitmq.queue}";
    public static final String RABBITMQ_EXCHANGE = "${rabbitmq.exchange}";
    public static final String RABBITMQ_ROUTING_KEY = "${rabbitmq.routing-key}";
    public static final String WEATHERSTACK_API_KEY = "${weatherstack.api-key}";
    public static final String JWT_SECRET = "${jwt.secret}";
    public static final String JWT_EXPIRATION = "${jwt.expiration}";
    public static final String BREVO_API_KEY = "${brevo.api-key}";
    public static final String BREVO_API_URL = "${brevo.url}";
    public static final String BREVO_EMAIL_DOMAIN = "${brevo.domain}";
    public static final String BREVO_SENDER_NAME = "${brevo.name}";

}