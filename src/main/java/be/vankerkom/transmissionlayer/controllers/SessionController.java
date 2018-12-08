package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.EntityNotFoundException;
import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatisticsDto;
import be.vankerkom.transmissionlayer.services.TransmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private TransmissionService transmissionService;

    @GetMapping
    public ResponseEntity getSession() {
        final Optional<Object> session = transmissionService.getSession();

        return session.map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Could not fetch session information."));
    }

    @GetMapping("/stats")
    public ResponseEntity getSessionStats() {
        final Optional<SessionStatisticsDto> sessionStats = transmissionService.getSessionStats();

        return sessionStats.map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Could not fetch session statistics"));
    }

}
