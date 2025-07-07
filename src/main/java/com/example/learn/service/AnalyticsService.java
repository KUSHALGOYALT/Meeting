package com.example.learn.service;

import com.example.learn.dto.EmotionDataRequest;
import com.example.learn.dto.FatigueDataRequest;
import com.example.learn.dto.HeadPoseDataRequest;
import com.example.learn.model.EmotionEntry;
import com.example.learn.model.FatigueEntry;
import com.example.learn.model.HeadPoseEntry;
import com.example.learn.model.MeetingAnalyticsSnapshot;
import com.example.learn.repository.EmotionEntryRepository;
import com.example.learn.repository.FatigueEntryRepository;
import com.example.learn.repository.HeadPoseEntryRepository;
import com.example.learn.repository.MeetingAnalyticsSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AnalyticsService {

    @Autowired
    private EmotionEntryRepository emotionEntryRepository;

    @Autowired
    private FatigueEntryRepository fatigueEntryRepository;

    @Autowired
    private HeadPoseEntryRepository headPoseEntryRepository;

    @Autowired
    private MeetingAnalyticsSnapshotRepository snapshotRepository;

    public void recordEmotionData(EmotionDataRequest request) {
        EmotionEntry entry = new EmotionEntry();
        entry.setId(UUID.randomUUID().toString());
        entry.setParticipantId(request.getParticipantId());
        entry.setMeetingId(request.getMeetingId());
        entry.setEmotion(request.getEmotion());
        entry.setScore(request.getScore());
        entry.setTimestamp(request.getTimestamp());
        emotionEntryRepository.save(entry);
    }

    public void recordFatigueData(FatigueDataRequest request) {
        FatigueEntry entry = new FatigueEntry();
        entry.setId(UUID.randomUUID().toString());
        entry.setParticipantId(request.getParticipantId());
        entry.setMeetingId(request.getMeetingId());
        entry.setStatus(request.getStatus());
        entry.setScore(request.getScore());
        entry.setTimestamp(request.getTimestamp());
        fatigueEntryRepository.save(entry);
    }

    public void recordHeadPoseData(HeadPoseDataRequest request) {
        HeadPoseEntry entry = new HeadPoseEntry();
        entry.setId(UUID.randomUUID().toString());
        entry.setParticipantId(request.getParticipantId());
        entry.setMeetingId(request.getMeetingId());
        entry.setYaw(request.getYaw());
        entry.setPitch(request.getPitch());
        entry.setRoll(request.getRoll());
        entry.setTimestamp(request.getTimestamp());
        headPoseEntryRepository.save(entry);
    }

    public void generateAnalyticsSnapshot(String meetingId) {
        MeetingAnalyticsSnapshot snapshot = new MeetingAnalyticsSnapshot();
        snapshot.setId(UUID.randomUUID().toString());
        snapshot.setMeetingId(meetingId);
        snapshot.setTimestamp(new java.util.Date().toString());
        snapshot.setSummary("Snapshot for meeting " + meetingId); // Replace with actual summary logic
        snapshotRepository.save(snapshot);
    }

    public List<MeetingAnalyticsSnapshot> getMeetingAnalytics(String meetingId) {
        return snapshotRepository.findByMeetingId(meetingId);
    }

    public List<EmotionEntry> getParticipantEmotions(String participantId) {
        return emotionEntryRepository.findByParticipantId(participantId);
    }

    public List<FatigueEntry> getParticipantFatigue(String participantId) {
        return fatigueEntryRepository.findByParticipantId(participantId);
    }

    public List<HeadPoseEntry> getParticipantHeadPose(String participantId) {
        return headPoseEntryRepository.findByParticipantId(participantId);
    }
}