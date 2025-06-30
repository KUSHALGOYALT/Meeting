package com.example.learn.service;

import com.example.learn.model.EmotionData;
import com.example.learn.model.SwotAnalysis;
import com.example.learn.repository.SwotAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SwotService {
    @Autowired
    private EmotionService emotionService;
    @Autowired
    private SwotAnalysisRepository swotRepository;

    public SwotAnalysis generateSimpleSwot(String studentId, String subject, List<EmotionData> emotions) {
        emotions = emotionService.getStudentEmotions(studentId);
        // Process emotions to generate SWOT
        SwotAnalysis swot = new SwotAnalysis();
        swot.setId(UUID.randomUUID().toString());
        swot.setStudentId(studentId);
        swot.setSubject(subject);
        swot.setStrengths(emotions.stream()
                .filter(e -> e.getEmotion().equals("engaged"))
                .map(e -> "Engaged in " + subject + " at " + e.getTimestamp())
                .toList());
        swot.setWeaknesses(emotions.stream()
                .filter(e -> e.getEmotion().equals("confused"))
                .map(e -> "Confused in " + subject + " at " + e.getTimestamp())
                .toList());
        swot.setOpportunities(List.of("Additional practice sessions"));
        swot.setThreats(List.of("Time management issues"));
        swot.setAiRecommendations("Focus on areas of confusion with targeted exercises.");
        return swotRepository.save(swot);
    }

    public SwotAnalysis createSwotAnalysis(SwotAnalysis swot) {
        return null;
    }

    public List<SwotAnalysis> getStudentSwot(String studentId) {
        return null;
    }
}