package com.example.learn.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NlpServiceImpl implements NlpService {
    private static final Logger log = LoggerFactory.getLogger(NlpServiceImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String pythonScriptPath = "C:\\Users\\DELL\\Music\\learn\\src\\main\\resources\\python\\nlp_processor.py";

    @Override
    public List<String> generateLearningPaths(List<String> weaknesses, List<String> opportunities) {
        try {
            Process process = new ProcessBuilder(
                    "python",
                    pythonScriptPath,
                    "learning_paths",
                    objectMapper.writeValueAsString(weaknesses),
                    objectMapper.writeValueAsString(opportunities)
            ).start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.lines().collect(Collectors.joining());
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorOutput = errorReader.lines().collect(Collectors.joining());
            if (!errorOutput.isEmpty()) {
                log.error("Python script error: {}", errorOutput);
                throw new RuntimeException("Python script failed: " + errorOutput);
            }

            return objectMapper.readValue(output, List.class);
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
            Process process = new ProcessBuilder(
                    "python",
                    pythonScriptPath,
                    "answer_question",
                    question,
                    context,
                    objectMapper.writeValueAsString(additionalData != null ? additionalData : List.of())
            ).start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.lines().collect(Collectors.joining());
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorOutput = errorReader.lines().collect(Collectors.joining());
            if (!errorOutput.isEmpty()) {
                log.error("Python script error: {}", errorOutput);
                throw new RuntimeException("Python script failed: " + errorOutput);
            }

            return objectMapper.readValue(output, String.class);
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

            Process process = new ProcessBuilder(
                    "python",
                    pythonScriptPath,
                    "adaptive_quiz",
                    subject,
                    weakness,
                    sessionId,
                    studentId
            ).start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.lines().collect(Collectors.joining());
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorOutput = errorReader.lines().collect(Collectors.joining());
            if (!errorOutput.isEmpty()) {
                log.error("Python script error: {}", errorOutput);
                throw new RuntimeException("Python script failed: " + errorOutput);
            }

            quizQuestions.addAll(objectMapper.readValue(output, List.class));
            quizQuestions.add("Practice problem: Solve a " + weakness + " exercise.");
            return quizQuestions;
        } catch (Exception e) {
            log.warn("Error generating quiz for session {}: {}", sessionId, e.getMessage());
            quizQuestions.add("Error generating quiz: " + e.getMessage());
            return quizQuestions;
        }
    }
}