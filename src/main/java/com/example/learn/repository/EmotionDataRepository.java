package com.example.learn.repository;

import com.example.learn.model.EmotionData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmotionDataRepository extends MongoRepository<EmotionData, String> {
    List<EmotionData> findByStudentId(String studentId);
    List<EmotionData> findByMeetingId(String meetingId);
    List<EmotionData> findByStudentIdAndMeetingId(String studentId, String meetingId);
    List<EmotionData> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
