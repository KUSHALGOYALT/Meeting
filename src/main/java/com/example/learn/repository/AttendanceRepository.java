package com.example.learn.repository;

import com.example.learn.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findByMeetingId(String meetingId);
    List<Attendance> findByStudentId(String studentId);
    Attendance findByMeetingIdAndStudentId(String meetingId, String studentId);
}
