package com.journalize.journalize.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.journalize.journalize.constants.Constants;
import com.journalize.journalize.dto.email.EmailResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value(Constants.BREVO_API_KEY)
    private String apiKey;

    @Value(Constants.BREVO_API_URL)
    private String url;

    @Value(Constants.BREVO_EMAIL_DOMAIN)
    private String domain;

    @Value(Constants.BREVO_SENDER_NAME)
    private String name;

    private final RestClient restClient;

    public String sendEmail(String to, String subject, String body) {

        var SenderEmail = "journalize@" + domain;

        Map<String, Object> payload = Map.of(
                "sender", Map.of(
                        "name", name,
                        "email", SenderEmail),
                "to", List.of(
                        Map.of("email", to)),
                "subject", subject,
                "htmlContent", body);

        try {

            var response = restClient.post()
                    .uri(url)
                    .header("api-key", apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(payload)
                    .retrieve()
                    .toEntity(EmailResponse.class);

            if (response.getBody() == null || response.getBody().getMessageId() == null) {
                log.error("Brevo response missing messageId for recipient: {}", to);
                return null;
            }

            return response.getBody().getMessageId();

        } catch (Exception ex) {
            log.error("Failed to send email via Brevo", ex);
            return null;
        }
    }

}