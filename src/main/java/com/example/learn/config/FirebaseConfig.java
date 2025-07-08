package com.example.learn.config;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void initialize() throws IOException {
        String serviceAccountPath = System.getenv("FIREBASE_SERVICE_ACCOUNT");
        if (serviceAccountPath == null) {
            // Fallback to hardcoded path for development
            serviceAccountPath = "C:\\Users\\DELL\\Downloads\\firebase123.json";
            System.out.println("Using fallback Firebase service account path: " + serviceAccountPath);
        }
        
        try {
            FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://emotionattendanceapp.firebaseio.com")
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully");
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize Firebase: " + e.getMessage());
            System.err.println("Please ensure the Firebase service account file exists at: " + serviceAccountPath);
            // Don't throw the exception to allow the app to start without Firebase
        }
    }
}