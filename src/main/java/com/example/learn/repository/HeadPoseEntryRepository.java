package com.example.learn.repository;

import com.example.learn.model.HeadPoseEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
//done
public interface HeadPoseEntryRepository extends MongoRepository<HeadPoseEntry, String> {
    List<HeadPoseEntry> findByParticipantId(String participantId);
    List<HeadPoseEntry> findByMeetingId(String meetingId);
}