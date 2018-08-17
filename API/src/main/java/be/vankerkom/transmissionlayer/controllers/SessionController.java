package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatistics;
import be.vankerkom.transmissionlayer.services.TransmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private TransmissionService transmissionService;

    @GetMapping
    public ResponseEntity getSession() {
        final Object session = transmissionService.getSession();

        return ResponseEntity.ok(session);
    }

    @GetMapping("/stats")
    public ResponseEntity getSessionStats() {
        final SessionStatistics sessionStats = transmissionService.getSessionStats();

        return ResponseEntity.ok(sessionStats);
    }

}
