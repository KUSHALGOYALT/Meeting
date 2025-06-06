package com.example.learn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class LearningPathService {

    private final NlpService nlpService;

    @Autowired
    public LearningPathService(NlpService nlpService) {
        this.nlpService = nlpService;
    }

    public String processQuestion(String sessionId, String studentId, String question) {
        String context = "Student " + studentId + " in session " + sessionId + " needs help with math.";
        List<String> additionalData = Arrays.asList("algebra", "calculus");
        return nlpService.answerQuestion(question, context, additionalData);
    }

    public List<String> generateQuiz(String sessionId, String studentId) {
        // Example student profile
        HashMap<Object, Object> studentProfile = new HashMap<>();
        studentProfile.put("subject", "algebra");
        studentProfile.put("weakness", "linear equations");
        studentProfile.put("strength", "geometry");

        // Return List<String> directly
        return nlpService.generateAdaptiveQuiz(sessionId, studentId, studentProfile);
    }

    // Optional: If a String is needed, join the list
    public String generateQuizAsString(String sessionId, String studentId) {
        List<String> quizQuestions = generateQuiz(sessionId, studentId);
        return String.join("\n", quizQuestions); // Join questions with newlines
    }
}