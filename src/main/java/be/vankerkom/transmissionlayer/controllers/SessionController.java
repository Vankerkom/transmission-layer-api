package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.EntityNotFoundException;
import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatisticsDto;
import be.vankerkom.transmissionlayer.services.TransmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private TransmissionService transmissionService;

    @GetMapping
    public Object getSession() {
        return transmissionService.getSession()
                .orElseThrow(() -> new EntityNotFoundException("Could not fetch session information."));
    }

    @GetMapping("/stats")
    public SessionStatisticsDto getSessionStats() {
        return transmissionService.getSessionStats()
                .orElseThrow(() -> new EntityNotFoundException("Could not fetch session statistics"));
    }

}
