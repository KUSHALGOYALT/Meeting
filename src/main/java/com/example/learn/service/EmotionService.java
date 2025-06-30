package com.example.learn.service;

import com.example.learn.model.EmotionData;
import com.example.learn.repository.EmotionDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmotionService {
    @Autowired
    private EmotionDataRepository emotionRepository;

    public EmotionData saveEmotionData(EmotionData emotionData) {
        return emotionRepository.save(emotionData);
    }

    public List<EmotionData> getStudentEmotions(String studentId) {
        return emotionRepository.findByStudentId(studentId);
    }

    public List<EmotionData> getMeetingEmotions(String meetingId) {
        return emotionRepository.findByMeetingId(meetingId);
    }

    public String getOverallMood(String studentId, String meetingId) {
        List<EmotionData> emotions = emotionRepository.findByStudentIdAndMeetingId(studentId, meetingId);
        if (emotions.isEmpty()) return "neutral";

        // Simple mood calculation
        int engaged = 0, neutral = 0, bored = 0, confused = 0;
        for (EmotionData emotion : emotions) {
            switch (emotion.getEmotion().toLowerCase()) {
                case "engaged": engaged++; break;
                case "neutral": neutral++; break;
                case "bored": bored++; break;
                case "confused": confused++; break;
            }
        }

        if (engaged >= neutral && engaged >= bored && engaged >= confused) return "engaged";
        if (bored >= neutral && bored >= confused) return "bored";
        if (confused >= neutral) return "confused";
        return "neutral";
    }
}
