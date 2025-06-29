package com.example.learn.service;

import java.util.HashMap;
import java.util.List;

public interface NlpService {
    List<String> generateLearningPaths(List<String> weaknesses, List<String> opportunities);
    String answerQuestion(String question, String context, List<String> additionalData);
    List<String> generateAdaptiveQuiz(String sessionId, String studentId, HashMap<Object, Object> studentProfile);
    String getSessionAnalytics(String sessionId, String studentId);
    String detectEmotion(String userId, String sessionId);
}