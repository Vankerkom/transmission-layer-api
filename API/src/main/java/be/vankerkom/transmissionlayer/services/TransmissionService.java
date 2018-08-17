package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.dto.TransmissionResponseGeneric;
import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatistics;

import java.util.Map;

public interface TransmissionService {

    Object getSession();

    SessionStatistics getSessionStats();

    TransmissionResponseGeneric getResource(String method);

    TransmissionResponseGeneric getResource(String method, Map<String, Object> arguments);

}
