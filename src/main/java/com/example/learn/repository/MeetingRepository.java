package com.example.learn.repository;

import com.example.learn.model.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends MongoRepository<Meeting, String> {
    List<Meeting> findByTeacherId(String teacherId);
    List<Meeting> findByStatus(String status);
    List<Meeting> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
