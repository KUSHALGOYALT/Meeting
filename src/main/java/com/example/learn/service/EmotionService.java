package com.example.learn.service;

import com.example.learn.model.Engagement;
import com.example.learn.repository.EngagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmotionService {
    @Autowired
    private EngagementRepository engagementRepository;
    @Autowired
    private FacialAnalysisService facialAnalysisService;

    public void updateEmotionFeedback(String sessionId, String studentId) {
        Engagement engagement = engagementRepository.findBySessionIdAndStudentId(sessionId, studentId)
                .orElse(new Engagement());
        Map<String, Double> emotions = facialAnalysisService.analyzeEmotions(studentId);
        engagement.setSessionId(sessionId);
        engagement.setStudentId(studentId);
        engagement.setFrustrationScore(emotions.getOrDefault("frustration", 0.0));
        engagement.setConfidenceScore(emotions.getOrDefault("confidence", 0.0));
        engagementRepository.save(engagement);
    }
}