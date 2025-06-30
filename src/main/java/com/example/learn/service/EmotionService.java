package com.example.learn.service;

import com.example.learn.model.EmotionData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmotionService {
    // Mock implementation for testing; replace with actual data source
    public List<EmotionData> getStudentEmotions(String studentId) {
        return List.of(
                new EmotionData(UUID.randomUUID().toString(), studentId, "engaged", LocalDateTime.now()),
                new EmotionData(UUID.randomUUID().toString(), studentId, "bored", LocalDateTime.now()),
                new EmotionData(UUID.randomUUID().toString(), studentId, "confused", LocalDateTime.now())
        );
    }

    public EmotionData saveEmotionData(EmotionData emotionData) {
        return null;
    }

    public List<EmotionData> getMeetingEmotions(String meetingId) {
        return null;
    }

    public String getOverallMood(String studentId, String meetingId) {
        return null;
    }
}