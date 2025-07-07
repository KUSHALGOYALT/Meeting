package com.example.learn.repository;

import com.example.learn.model.AttendanceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
//done
public interface AttendanceRecordRepository extends MongoRepository<AttendanceRecord, String> {
    List<AttendanceRecord> findByMeetingId(String meetingId);
    List<AttendanceRecord> findByParticipantEmail(String participantEmail);
}