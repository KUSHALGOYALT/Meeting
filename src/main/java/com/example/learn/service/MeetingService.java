package com.example.learn.service;
import com.example.learn.model.Attendance;
import com.example.learn.model.Session;
import com.example.learn.repository.AttendanceRepository;
import com.example.learn.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Service
@EnableScheduling
public class MeetingService {

    private static final Logger log = LoggerFactory.getLogger(MeetingService.class);

    private final SessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final NlpService nlpService;

    public MeetingService(SessionRepository sessionRepository,
                          AttendanceRepository attendanceRepository,
                          NlpService nlpService) {
        this.sessionRepository = sessionRepository;
        this.attendanceRepository = attendanceRepository;
        this.nlpService = nlpService;
    }

    public String createMeeting(String sessionId, String userId, String userName) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        String roomName = "JitsiMeet_" + sessionId + "_" + UUID.randomUUID().toString();
        session.setMeetingRoom(roomName);
        sessionRepository.save(session);

        Attendance attendance = new Attendance();
        attendance.setSessionId(sessionId);
        attendance.setUserId(userId);
        attendance.setName(userName);
        attendance.setJoinedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        attendance.setPresent(true);

        Attendance.EmotionEntry initialEmotion = new Attendance.EmotionEntry();
        initialEmotion.setEmotion("neutral");
        initialEmotion.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        attendance.getEmotions().add(initialEmotion);
        attendanceRepository.save(attendance);

        return roomName;
    }

    public void recordParticipantLeft(String sessionId, String userId) {
        Attendance attendance = attendanceRepository.findBySessionId(sessionId).stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found for user: " + userId));

        attendance.setLeftAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        attendance.setPresent(false);
        attendanceRepository.save(attendance);
    }

    public void updateEmotion(String sessionId, String userId) {
        Attendance attendance = attendanceRepository.findBySessionId(sessionId).stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found for user: " + userId));

        String emotion = nlpService.detectEmotion(userId, sessionId);
        Attendance.EmotionEntry emotionEntry = new Attendance.EmotionEntry();
        emotionEntry.setEmotion(emotion);
        emotionEntry.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        attendance.getEmotions().add(emotionEntry);
        attendanceRepository.save(attendance);
    }

    public Optional<Attendance> getStudentAttendance(String sessionId, String userId) {
        return attendanceRepository.findBySessionId(sessionId).stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst();
    }

    public Map<String, Object> getAttendanceSummary(String sessionId) {
        List<Attendance> all = attendanceRepository.findBySessionId(sessionId);
        long presentCount = all.stream().filter(Attendance::isPresent).count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("total", all.size());
        summary.put("present", presentCount);
        summary.put("absent", all.size() - presentCount);
        return summary;
    }

    public String generateAttendanceCsv(String sessionId) {
        List<Attendance> attendanceList = attendanceRepository.findBySessionId(sessionId);
        StringBuilder csv = new StringBuilder("Name,Join Time,Leave Time,Emotions\n");

        for (Attendance att : attendanceList) {
            String emotions = att.getEmotions().stream()
                    .map(e -> e.getEmotion() + "@" + e.getTimestamp())
                    .reduce((a, b) -> a + "; " + b)
                    .orElse("N/A");
            csv.append(String.format("%s,%s,%s,%s\n",
                    att.getName() != null ? att.getName() : "Unknown",
                    att.getJoinedAt() != null ? att.getJoinedAt() : "N/A",
                    att.getLeftAt() != null ? att.getLeftAt() : "N/A",
                    emotions));
        }

        return csv.toString();
    }

    @Scheduled(fixedRate = 5000)
    public void updateAllActiveEmotions() {
        List<Session> activeSessions = sessionRepository.findAll().stream()
                .filter(s -> s.getEndTime() == null || s.getEndTime().isAfter(LocalDateTime.now()))
                .toList();

        for (Session session : activeSessions) {
            List<Attendance> attendances = attendanceRepository.findBySessionId(session.getId());
            for (Attendance attendance : attendances) {
                if (attendance.getLeftAt() == null) {
                    updateEmotion(session.getId(), attendance.getUserId());
                }
            }
        }
    }
}
