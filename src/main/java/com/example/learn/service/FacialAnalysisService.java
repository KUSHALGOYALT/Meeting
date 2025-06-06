package com.example.learn.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FacialAnalysisService {
    public Map<String, Double> analyzeEmotions(String studentId) {
        return Map.of("frustration", 0.3, "confidence", 0.6);
    }
}