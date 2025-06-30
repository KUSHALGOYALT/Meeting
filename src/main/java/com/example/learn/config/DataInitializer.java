package com.example.learn.config;

import com.example.learn.model.*;
import com.example.learn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private SwotAnalysisRepository swotRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) return;

        // Sample students
        List<User> students = Arrays.asList(
                new User("Rahul Sharma", "rahul@example.com", "default123", "STUDENT"),
                new User("Priya Patel", "priya@example.com", "default123", "STUDENT"),
                new User("Amit Kumar", "amit@example.com", "default123", "STUDENT"),
                new User("Sneha Gupta", "sneha@example.com", "default123", "STUDENT"),
                new User("Arjun Singh", "arjun@example.com", "default123", "STUDENT")
        );
        userRepository.saveAll(students);

        // Sample teachers
        List<User> teachers = Arrays.asList(
                new User("Dr. Sunita Mehta", "sunita@example.com", "default123", "TEACHER"),
                new User("Prof. Rajesh Verma", "rajesh@example.com", "default123", "TEACHER")
        );
        userRepository.saveAll(teachers);

        // Admin
        User admin = new User("Admin User", "admin@example.com", "admin123", "ADMIN");
        userRepository.save(admin);

        // Sample meetings
        Meeting m1 = new Meeting();
        m1.setTitle("Mathematics Class");
        m1.setTeacherId(teachers.get(0).getId());
        m1.setDescription("math-room-101");
        m1.setStartTime(LocalDateTime.now());
        m1.setEndTime(LocalDateTime.now().plusHours(1));

        Meeting m2 = new Meeting();
        m2.setTitle("Science Lab Session");
        m2.setTeacherId(teachers.get(1).getId());
        m2.setDescription("science-lab-202");
        m2.setStartTime(LocalDateTime.now());
        m2.setEndTime(LocalDateTime.now().plusHours(1));

        Meeting m3 = new Meeting();
        m3.setTitle("English Literature");
        m3.setTeacherId(teachers.get(0).getId());
        m3.setDescription("english-room-103");
        m3.setStartTime(LocalDateTime.now());
        m3.setEndTime(LocalDateTime.now().plusHours(1));

        meetingRepository.saveAll(Arrays.asList(m1, m2, m3));

        // Sample SWOTs
        SwotAnalysis swot1 = new SwotAnalysis(students.get(0).getId(), "Mathematics");
        swot1.setStrengths(Arrays.asList("Good analytical skills", "Quick problem solving"));
        swot1.setWeaknesses(Arrays.asList("Struggles with complex equations"));
        swot1.setOpportunities(Arrays.asList("Extra practice sessions", "Peer tutoring"));
        swot1.setThreats(Arrays.asList("Time management issues"));
        swot1.setAiRecommendations("Focus on step-by-step problem solving approach");

        SwotAnalysis swot2 = new SwotAnalysis(students.get(1).getId(), "Science");
        swot2.setStrengths(Arrays.asList("Excellent memory", "Strong theoretical knowledge"));
        swot2.setWeaknesses(Arrays.asList("Lacks practical application skills"));
        swot2.setOpportunities(Arrays.asList("More lab sessions", "Hands-on experiments"));
        swot2.setThreats(Arrays.asList("Overconfidence in theory"));
        swot2.setAiRecommendations("Balance theory with practical applications");

        swotRepository.saveAll(Arrays.asList(swot1, swot2));

        System.out.println("Sample data initialized successfully!");
    }
}
