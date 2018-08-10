package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.dto.TransmissionResponse;

import java.util.Map;

public interface TransmissionService {

    Object getSession();

    TransmissionResponse getResource(String method);

    TransmissionResponse getResource(String method, Map<String, Object> arguments);

}
