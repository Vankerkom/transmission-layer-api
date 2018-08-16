package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.dto.TransmissionRequest;
import be.vankerkom.transmissionlayer.models.dto.TransmissionResponse;
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
        TransmissionResponse response = getResource("session-get");

        if (response.isSuccess()) {
            return response.getArguments();
        }

        return null;
    }

    public TransmissionResponse getResource(String method) {
        return this.getResource(method, null);
    }

    public TransmissionResponse getResource(String method, Map<String, Object> arguments) {
        ResponseEntity<TransmissionResponse> response = restTemplate.postForEntity(
                resourceHost,
                new TransmissionRequest(method, arguments),
                TransmissionResponse.class
        );

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method: {}, Response Status Code: {}", method, response.getStatusCode());
        }

        return response.getBody();
    }

}
