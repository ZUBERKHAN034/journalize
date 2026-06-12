package com.journalize.journalize.schedulers;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.journalize.journalize.constants.Constants;
import com.journalize.journalize.entities.User;
import com.journalize.journalize.repositories.UserRepository;
import com.journalize.journalize.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserScheduler {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    @Scheduled(cron = "0 0 0 * * SUN") // Run every Sunday at 00:00 UTC
    public void sendWeeklySentimentEmailToUserScheduler() {

        var users = userRepository.getUsersForSA();

        for (var user : users) {

            var sentiment = getUserWeeklySentiment(user);

            if (sentiment == null)
                sentiment = "NONE";

            Context context = new Context();
            context.setVariable("firstName", user.getFirstName());
            context.setVariable("weeklySentiment", sentiment);
            context.setVariable("weeklySentimentMessage", getWeeklySentimentMessage(sentiment));

            var html = templateEngine.process(Constants.EMAIL_TEMPLATE_WEEKLY_SENTIMENT, context);

            var messageId = emailService.sendEmail(user.getEmail(), "Journalize Weekly Sentiment Report", html);
            if (messageId != null) {
                log.info("Email sent with messageId: {}", messageId);
            } else {
                log.error("Failed to send email for user: {}", user.getEmail());
            }
        }

        log.info("Weekly sentiment email sent to {} users", users.size());

    }

    private String getUserWeeklySentiment(User user) {

        var sentimentCount = user.getJournals().stream()
                .filter(journal -> journal.getCreatedAt()
                        .isAfter(LocalDateTime.now().minusDays(7)))
                .map(journal -> journal.getSentiment())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()));

        return sentimentCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().name())
                .orElse(null);
    }

    private String getWeeklySentimentMessage(String sentiment) {

        return switch (sentiment) {
            case "HAPPY" -> """
                    Your journal entries reflected a positive and uplifting outlook this week.
                    Keep nurturing the habits and experiences that contribute to your well-being.
                    """;

            case "SAD" -> """
                    Your journal entries suggested some difficult emotions this week.
                    Remember to be patient with yourself and take time to care for your emotional well-being.
                    """;

            case "NEUTRAL" -> """
                    Your journal entries reflected a balanced and steady outlook this week.
                    Consistent reflection is a great way to build self-awareness and personal growth.
                    """;

            case "ANGRY" ->
                """
                        Your journal entries reflected feelings of frustration or anger this week.
                        Taking time to understand and process these emotions can help you navigate challenges more effectively.
                        """;

            case "ANXIOUS" -> """
                    Your journal entries indicated feelings of worry or uncertainty this week.
                    Consider taking moments for rest, mindfulness, and activities that help you feel grounded.
                    """;

            default -> """
                    We couldn't determine a dominant sentiment from your journal entries this week.
                    Keep journaling regularly to gain more meaningful insights over time.
                    """;
        };
    }

}