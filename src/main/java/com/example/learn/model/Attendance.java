package com.example.learn.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "attendance")
@Data
public class Attendance {
    @Id
    private String studentId; // ✅ field must match
    private String id;
    private String sessionId;
    private String userId;
    private String name;
    private String joinedAt;
    private String leftAt;
    private boolean present;
    private List<EmotionEntry> emotions = new ArrayList<>();

    @Data
    public static class EmotionEntry {
        private String emotion;
        private String timestamp;
    }
}
