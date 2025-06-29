package com.example.learn.controller;

import com.example.learn.exception.*;
import com.example.learn.model.Session;
import com.example.learn.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/attendance/{sessionId}/{studentId}/login")
    public ResponseEntity<?> recordAttendanceLogin(@PathVariable String sessionId, @PathVariable String studentId,
                                                   Authentication auth) {
        try {
            checkStudentOrTeacher(auth);
            sessionService.recordAttendanceLogin(sessionId, studentId);
            EntityModel<Map<String, String>> response = EntityModel.of(
                    Map.of("message", "Login time recorded"),
                    linkTo(methodOn(SessionController.class).recordAttendanceLogin(sessionId, studentId, auth)).withSelfRel(),
                    linkTo(methodOn(SessionController.class).recordAttendanceLogout(sessionId, studentId, auth)).withRel("logout"),
                    linkTo(methodOn(SessionController.class).getTeacherDashboard(sessionId)).withRel("dashboard")
            );
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildErrorResponse(e));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(e));
        } catch (DatabaseException | OfflineSyncException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse(e));
        }
    }

    @PostMapping("/attendance/{sessionId}/{studentId}/logout")
    public ResponseEntity<?> recordAttendanceLogout(@PathVariable String sessionId, @PathVariable String studentId,
                                                    Authentication auth) {
        try {
            checkStudentOrTeacher(auth);
            sessionService.recordAttendanceLogout(sessionId, studentId);
            EntityModel<Map<String, String>> response = EntityModel.of(
                    Map.of("message", "Logout time recorded and attendance marked"),
                    linkTo(methodOn(SessionController.class).recordAttendanceLogout(sessionId, studentId, auth)).withSelfRel(),
                    linkTo(methodOn(SessionController.class).getTeacherDashboard(sessionId)).withRel("dashboard")
            );
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildErrorResponse(e));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(e));
        } catch (DatabaseException | OfflineSyncException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse(e));
        }
    }

    @PostMapping("/weekly-quiz/{sessionId}")
    public ResponseEntity<?> generateWeeklyQuiz(@PathVariable String sessionId, Authentication auth) {
        try {
            checkTeacherOrAdmin(auth);
            sessionService.generateWeeklyQuiz(sessionId);
            EntityModel<Map<String, String>> response = EntityModel.of(
                    Map.of("message", "Weekly quiz generated"),
                    linkTo(methodOn(SessionController.class).generateWeeklyQuiz(sessionId, auth)).withSelfRel(),
                    linkTo(methodOn(SessionController.class).getSessions()).withRel("sessions"),
                    linkTo(methodOn(SessionController.class).getTeacherDashboard(sessionId)).withRel("dashboard")
            );
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildErrorResponse(e));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(e));
        } catch (DatabaseException | OfflineSyncException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse(e));
        }
    }

    @PostMapping("/weekly-quiz/{quizId}/answers/{studentId}")
    public ResponseEntity<?> submitWeeklyQuizAnswers(@PathVariable String quizId, @PathVariable String studentId,
                                                     @RequestBody Map<String, Boolean> answers, Authentication auth) {
        try {
            checkStudent(auth);
            sessionService.submitWeeklyQuizAnswers(quizId, studentId, answers);
            EntityModel<Map<String, String>> response = EntityModel.of(
                    Map.of("message", "Weekly quiz answers submitted"),
                    linkTo(methodOn(SessionController.class).submitWeeklyQuizAnswers(quizId, studentId, answers, auth)).withSelfRel(),
                    linkTo(methodOn(SessionController.class).getTeacherDashboard(quizId)).withRel("dashboard")
            );
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildErrorResponse(e));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(e));
        } catch (DatabaseException | OfflineSyncException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse(e));
        }
    }

    @PostMapping("/session")
    public ResponseEntity<?> saveSession(@RequestBody Session session, Authentication auth) {
        try {
            checkTeacherOrAdmin(auth);
            sessionService.saveSession(session);
            EntityModel<Session> response = EntityModel.of(session,
                    linkTo(methodOn(SessionController.class).saveSession(session, auth)).withSelfRel(),
                    linkTo(methodOn(SessionController.class).getSessions()).withRel("sessions"),
                    linkTo(methodOn(SessionController.class).generateWeeklyQuiz(session.getId(), auth)).withRel("generate-weekly-quiz")
            );
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildErrorResponse(e));
        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse(e));
        }
    }

    @GetMapping("/sessions")
    public ResponseEntity<?> getSessions() {
        try {
            List<Session> sessions = sessionService.getAllSessions(null, null, null, null, null);
            List<EntityModel<Session>> sessionModels = sessions.stream()
                    .map(session -> EntityModel.of(session,
                            linkTo(methodOn(SessionController.class).getSessions()).withSelfRel(),
                            linkTo(methodOn(SessionController.class).generateWeeklyQuiz(session.getId(), null)).withRel("generate-weekly-quiz"),
                            linkTo(methodOn(SessionController.class).getTeacherDashboard(session.getId())).withRel("dashboard")))
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<Session>> response = CollectionModel.of(sessionModels,
                    linkTo(methodOn(SessionController.class).getSessions()).withSelfRel());
            return ResponseEntity.ok(response);
        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse(e));
        }
    }

    @GetMapping("/dashboard/{sessionId}")
    public ResponseEntity<?> getTeacherDashboard(@PathVariable String sessionId) {
        try {
            Map<String, Object> dashboard = sessionService.getTeacherDashboard(sessionId);
            EntityModel<Map<String, Object>> response = EntityModel.of(dashboard,
                    linkTo(methodOn(SessionController.class).getTeacherDashboard(sessionId)).withSelfRel(),
                    linkTo(methodOn(SessionController.class).getSessions()).withRel("sessions"),
                    linkTo(methodOn(SessionController.class).generateWeeklyQuiz(sessionId, null)).withRel("generate-weekly-quiz")
            );
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(e));
        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse(e));
        }
    }

    private void checkTeacherOrAdmin(Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_TEACHER"))) {
            throw new AccessDeniedException("Access restricted to teachers or admins");
        }
    }

    private void checkStudentOrTeacher(Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT") || a.getAuthority().equals("ROLE_TEACHER") || a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Access restricted to students, teachers, or admins");
        }
    }

    private void checkStudent(Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))) {
            throw new AccessDeniedException("Access restricted to students");
        }
    }

    private EntityModel<Map<String, String>> buildErrorResponse(Exception e) {
        return EntityModel.of(Map.of("error", e.getMessage()),
                linkTo(methodOn(SessionController.class).getSessions()).withRel("sessions"));
    }
}
