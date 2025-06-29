package com.example.learn.controller;
import com.example.learn.exception.AccessDeniedException;
import com.example.learn.exception.DatabaseException;
import com.example.learn.exception.NotFoundException;
import com.example.learn.model.SwotAnalysis;
import com.example.learn.service.SwotAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@RestController
@RequestMapping("/api/swot")
public class SwotAnalysisController {
    @Autowired
    private SwotAnalysisService swotAnalysisService;
    @PostMapping("/{sessionId}/{studentId}")
    public ResponseEntity<?> generateSwotAnalysis(@PathVariable String sessionId, @PathVariable String studentId,
                                                  Authentication auth) {
        try {
            checkTeacherOrAdmin(auth);
            SwotAnalysis swot = swotAnalysisService.generateSwotAnalysis(sessionId, studentId);
            EntityModel<SwotAnalysis> response = EntityModel.of(swot,
                    linkTo(methodOn(SwotAnalysisController.class).generateSwotAnalysis(sessionId, studentId, auth)).withSelfRel(),
                    linkTo(methodOn(SessionController.class).getTeacherDashboard(sessionId)).withRel("dashboard"),
                    linkTo(methodOn(SessionController.class).getSessions()).withRel("sessions")
            );
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildErrorResponse(e));
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
    private EntityModel<Map<String, String>> buildErrorResponse(Exception e) {
        return EntityModel.of(Map.of("error", e.getMessage()),
                linkTo(methodOn(SessionController.class).getSessions()).withRel("sessions"));
    }
}