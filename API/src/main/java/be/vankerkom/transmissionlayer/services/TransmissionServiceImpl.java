package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.dto.TransmissionRequest;
import be.vankerkom.transmissionlayer.models.dto.TransmissionResponse;
import be.vankerkom.transmissionlayer.models.dto.TransmissionResponseGeneric;
import be.vankerkom.transmissionlayer.models.dto.TransmissionResponseSessionStatistics;
import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatistics;
import be.vankerkom.transmissionlayer.transmission.TransmissionSessionIdInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TransmissionServiceImpl implements TransmissionService {

    private static final Logger LOG = LogManager.getLogger(TransmissionServiceImpl.class);

    @Value("${transmission.url}")
    private String resourceHost;

    private RestTemplate restTemplate;

    @Autowired
    public TransmissionServiceImpl(@Value("${transmission.username}") final String username,
                                   @Value("${transmission.password}") final String password,
                                   final RestTemplateBuilder restTemplateBuilder) {

        restTemplate = restTemplateBuilder
                .setConnectTimeout(1000)
                .setReadTimeout(1000)
                .interceptors(new TransmissionSessionIdInterceptor())
                .basicAuthorization(username, password)
                .build();

    }

    public Object getSession() {
        TransmissionResponseGeneric response = getResource("session-get");

        if (response.isSuccess()) {
            return response.getArguments();
        }

        return null;
    }

    @Override
    public SessionStatistics getSessionStats() {
        final TransmissionResponseSessionStatistics response = getResource("session-stats", TransmissionResponseSessionStatistics.class);

        if (response.isSuccess()) {
            return response.getArguments();
        }

        return null;
    }

    public TransmissionResponseGeneric getResource(final String method) {
        return getResource(method, (Map<String, Object>) null);
    }

    public TransmissionResponseGeneric getResource(final String method, final Map<String, Object> arguments) {
        ResponseEntity<TransmissionResponseGeneric> response = restTemplate.postForEntity(
                resourceHost,
                new TransmissionRequest(method, arguments),
                TransmissionResponseGeneric.class
        );

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method: {}, Response Status Code: {}", method, response.getStatusCode());
        }

        return response.getBody();
    }

    private <T extends TransmissionResponse<?>> T getResource(final String method, Class<T> clazz) {
        return getResource(method, null, clazz);
    }

    private <T extends TransmissionResponse<?>> T getResource(final String method, final Map<String, Object> arguments, Class<T> clazz) {
        ResponseEntity<T> response = restTemplate.postForEntity(
                resourceHost,
                new TransmissionRequest(method, arguments),
                clazz
        );

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method: {}, Response Status Code: {}", method, response.getStatusCode());
        }

        return response.getBody();
    }

}
