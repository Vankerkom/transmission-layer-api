package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.dto.TransmissionRequest;
import be.vankerkom.transmissionlayer.models.dto.TransmissionResponse;
import be.vankerkom.transmissionlayer.transmission.TransmissionResponseErrorHandler;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

@Service
public class TransmissionServiceImpl implements TransmissionService {

    private static final Logger LOG = LogManager.getLogger(TransmissionServiceImpl.class);

    @Value("${transmission.url}")
    private String resourceHost;

    @Value("${transmission.username}")
    private String resourceUsername;

    @Value("${transmission.password}")
    private String resourcePassword;

    @Autowired
    private Gson gson;

    private String sessionId = "";

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
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new TransmissionResponseErrorHandler());
        ResponseEntity<String> response = restTemplate.postForEntity(
                resourceHost,
                new HttpEntity<>(new TransmissionRequest(method, arguments), createHeaders()),
                String.class
        );

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method: {}, Response Status Code: {}", method, response.getStatusCode());
        }

        // Validate if the session id is invalid.
        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            // Read the new session id and resend the previous request.
            String newId = response.getHeaders()
                    .getOrDefault("X-Transmission-Session-Id", Collections.singletonList(""))
                    .get(0);

            sessionId = newId != null ? newId : "";

            return getResource(method, arguments);
        }

        // If only transmission set the correct response headers...
        return gson.fromJson(response.getBody(), TransmissionResponse.class);
    }

    private HttpHeaders createHeaders() {
        // headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        return new HttpHeaders() {{
            String auth = resourceUsername + ":" + resourcePassword;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
            set("X-Transmission-Session-Id", sessionId);
            set("Accept", "application/json");
            set("Content-Type", "application/json");
        }};
    }

}
