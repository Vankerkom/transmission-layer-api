package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.exceptions.EntityNotFoundException;
import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatisticsDto;
import be.vankerkom.transmissionlayer.services.TransmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {

    private final TransmissionService transmissionService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public Object getSession() {
        return transmissionService.getSession()
                .orElseThrow(() -> new EntityNotFoundException("Could not fetch session information."));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/stats")
    public SessionStatisticsDto getSessionStats() {
        return transmissionService.getSessionStats()
                .orElseThrow(() -> new EntityNotFoundException("Could not fetch session statistics"));
    }

}
