package com.example.learn.repository;

import com.example.learn.model.EmotionEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
//done
import java.util.List;

public interface EmotionEntryRepository extends MongoRepository<EmotionEntry, String> {
    List<EmotionEntry> findByParticipantId(String participantId);
    List<EmotionEntry> findByMeetingId(String meetingId);
}