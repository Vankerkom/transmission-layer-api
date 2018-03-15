package be.vankerkom.transmissionlayer.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

@Service
public class TransmissionService {

    private static final Logger LOG = LogManager.getLogger(TransmissionService.class);

    @Value("${transmission.url}")
    private String resourceHost;

    @Value("${transmission.username}")
    private String resourceUsername;

    @Value("${transmission.password}")
    private String resourcePassword;

    private String sessionId;

    // TODO Move
    private class TransmissionRequest implements Serializable {
        private String method;
        private Map<String, Object> arguments;
        private Integer tag;

        TransmissionRequest(String method, Map<String, Object> arguments) {
            this.method = method;
            this.arguments = arguments;
        }

        public String getMethod() {
            return method;
        }

        public Map<String, Object> getArguments() {
            return arguments;
        }

        public Integer getTag() {
            return tag;
        }
    }

    // TODO Move
    private class TransmissionResponse {
        private String result;

        private Object arguments;
        private int tag;

    }

    // TODO Move
    private class MyResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

        }

        @Override
        public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {

        }
    }

    public String getResource(String method) {
        return this.getResource(method, null);
    }

    public String getResource(String method, Map<String, Object> arguments) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new MyResponseErrorHandler());

        ResponseEntity<String> response = restTemplate.postForEntity(
                resourceHost,
                new HttpEntity<>(new TransmissionRequest(method, arguments), createHeaders()),
                String.class
        );

        LOG.debug("Status Code: " + response.getStatusCode());

        // Validate if the session id is invalid.
        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            // Read the new session id and resend the previous request.
            String newId = response.getHeaders().get("X-Transmission-Session-Id").get(0);
            sessionId = newId != null ? newId : "";

            return getResource(method, arguments);
        }

        return response.getBody();
    }

    private HttpHeaders createHeaders() {
        return new HttpHeaders() {{
            String auth = resourceUsername + ":" + resourcePassword;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
            set("X-Transmission-Session-Id", sessionId);
        }};
    }

}
