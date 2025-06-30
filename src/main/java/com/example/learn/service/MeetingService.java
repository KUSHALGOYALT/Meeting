package com.example.learn.service;

import com.example.learn.model.Meeting;
import com.example.learn.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public Meeting createMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public Meeting startMeeting(String meetingId) {
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);
        if (meeting.isPresent()) {
            Meeting m = meeting.get();
            m.setStatus("ACTIVE");
            m.setStartTime(LocalDateTime.now());
            return meetingRepository.save(m);
        }
        return null;
    }

    public Meeting endMeeting(String meetingId) {
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);
        if (meeting.isPresent()) {
            Meeting m = meeting.get();
            m.setStatus("COMPLETED");
            m.setEndTime(LocalDateTime.now());
            return meetingRepository.save(m);
        }
        return null;
    }
}
