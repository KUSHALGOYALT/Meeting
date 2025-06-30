package com.example.learn.service;

import com.example.learn.model.EmotionData;
import com.example.learn.model.SwotAnalysis;
import com.example.learn.repository.SwotAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwotService {
    @Autowired
    private SwotAnalysisRepository swotRepository;

    public SwotAnalysis createSwotAnalysis(SwotAnalysis swot) {
        return swotRepository.save(swot);
    }

    public List<SwotAnalysis> getStudentSwot(String studentId) {
        return swotRepository.findByStudentId(studentId);
    }

    public SwotAnalysis generateSimpleSwot(String studentId, String subject, List<EmotionData> emotions) {
        SwotAnalysis swot = new SwotAnalysis(studentId, subject);

        // Simple AI-like analysis based on emotions
        long engagedCount = emotions.stream().mapToLong(e -> "engaged".equals(e.getEmotion()) ? 1 : 0).sum();
        long boredCount = emotions.stream().mapToLong(e -> "bored".equals(e.getEmotion()) ? 1 : 0).sum();
        long confusedCount = emotions.stream().mapToLong(e -> "confused".equals(e.getEmotion()) ? 1 : 0).sum();

        // Generate basic SWOT
        if (engagedCount > emotions.size() * 0.6) {
            swot.setStrengths(List.of("High engagement level", "Active participation"));
        } else {
            swot.setWeaknesses(List.of("Low engagement", "Needs attention"));
        }

        if (confusedCount > emotions.size() * 0.3) {
            swot.setWeaknesses(List.of("Difficulty understanding concepts", "Needs additional support"));
            swot.setOpportunities(List.of("Extra tutoring sessions", "Visual learning aids"));
        }

        swot.setAiRecommendations("Based on emotion analysis: Focus on interactive content to maintain engagement.");

        return swotRepository.save(swot);
    }
}
