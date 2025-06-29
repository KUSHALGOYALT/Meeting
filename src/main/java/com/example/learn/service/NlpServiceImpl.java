package com.example.learn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NlpServiceImpl implements NlpService {
    private static final Logger log = LoggerFactory.getLogger(NlpServiceImpl.class);
    private final WebClient webClient;

    public NlpServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    @Override
    public List<String> generateLearningPaths(List<String> weaknesses, List<String> opportunities) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "weaknesses", weaknesses,
                    "opportunities", opportunities
            );

            Mono<List> response = webClient.post()
                    .uri("/nlp/learning-paths")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(List.class);

            return response.block();
        } catch (Exception e) {
            log.warn("Error generating learning paths: {}", e.getMessage());
            List<String> paths = new ArrayList<>();
            weaknesses.forEach(w -> paths.add("Address " + w + " with targeted exercises (NLP error)"));
            opportunities.forEach(o -> paths.add("Pursue " + o + " through advanced study (NLP error)"));
            return paths;
        }
    }

    @Override
    public String answerQuestion(String question, String context, List<String> additionalData) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "question", question,
                    "context", context,
                    "additionalData", additionalData != null ? additionalData : List.of()
            );

            Mono<Map> response = webClient.post()
                    .uri("/nlp/answer-question")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class);

            return (String) response.block().get("answer");
        } catch (Exception e) {
            log.warn("Error answering question '{}': {}", question, e.getMessage());
            return "Error processing question: " + e.getMessage();
        }
    }

    @Override
    public List<String> generateAdaptiveQuiz(String sessionId, String studentId, HashMap<Object, Object> studentProfile) {
        List<String> quizQuestions = new ArrayList<>();
        try {
            String subject = studentProfile.getOrDefault("subject", "unknown") instanceof String s ? s : "unknown";
            String weakness = studentProfile.getOrDefault("weakness", "unknown") instanceof String w ? w : "unknown";

            Map<String, Object> requestBody = Map.of(
                    "subject", subject,
                    "weakness", weakness,
                    "sessionId", sessionId,
                    "studentId", studentId
            );

            Mono<List> response = webClient.post()
                    .uri("/nlp/adaptive-quiz")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(List.class);

            quizQuestions.addAll(response.block());
            return quizQuestions;
        } catch (Exception e) {
            log.warn("Error generating quiz for session {}: {}", sessionId, e.getMessage());
            quizQuestions.add("Error generating quiz: " + e.getMessage());
            return quizQuestions;
        }
    }

    @Override
    public String getSessionAnalytics(String sessionId, String studentId) {
        try {
            Map<String, String> requestBody = Map.of(
                    "sessionId", sessionId,
                    "studentId", studentId
            );

            Mono<Map> response = webClient.post()
                    .uri("/nlp/session-analytics")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class);

            return (String) response.block().get("analytics");
        } catch (Exception e) {
            log.warn("Error generating session analytics for session {}: {}", sessionId, e.getMessage());
            return "Error generating analytics: " + e.getMessage();
        }
    }

    @Override
    public String detectEmotion(String userId, String sessionId) {
        try {
            log.debug("Calling FastAPI to detect emotion for userId: {}, sessionId: {}", userId, sessionId);
            Map<String, String> requestBody = Map.of(
                    "userId", userId,
                    "sessionId", sessionId
            );

            Mono<Map> response = webClient.post()
                    .uri("/nlp/detect-emotion")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class);

            Map<String, Object> result = response.block();
            String emotion = null;
            if (result != null) {
                Object emotionObj = result.get("emotion");
                if (emotionObj instanceof String) {
                    emotion = (String) emotionObj;
                } else if (emotionObj instanceof Map) {
                    emotion = ((Map<String, String>) emotionObj).getOrDefault("label", "neutral");
                }
            }
            emotion = emotion != null && !emotion.isEmpty() ? emotion : "neutral";
            log.debug("Emotion detected: {}", emotion);
            return emotion;
        } catch (Exception e) {
            log.error("Error detecting emotion for userId: {}, sessionId: {}, error: {}", userId, sessionId, e.getMessage());
            return "neutral";
        }
    }
}