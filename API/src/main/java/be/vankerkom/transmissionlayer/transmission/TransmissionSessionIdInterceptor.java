package be.vankerkom.transmissionlayer.transmission;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class TransmissionSessionIdInterceptor implements ClientHttpRequestInterceptor {

    private static final String SESSION_ID_HEADER_NAME = "X-Transmission-Session-Id";

    private String sessionId = "";

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
                                        final ClientHttpRequestExecution execution) throws IOException {

        ClientHttpResponse response = execute(request, body, execution);

        // Validate if the session id is invalid, if it's invalid then
        // read the new session id and resend the previous request.
        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            sessionId = getNewSessionId(response);

            response = execute(request, body, execution);
        }

        return response;
    }

    private ClientHttpResponse execute(final HttpRequest request, final byte[] body,
                                       final ClientHttpRequestExecution execution) throws IOException {

        // Update the session id header since it is required.
        request.getHeaders().set(SESSION_ID_HEADER_NAME, sessionId);

        return execution.execute(request, body);
    }

    private String getNewSessionId(final ClientHttpResponse response) throws IOException {
        final String newSessionId = response.getHeaders().getFirst(SESSION_ID_HEADER_NAME);

        if (StringUtils.isEmpty(newSessionId)) {
            throw new IOException("Session Id Header: " + SESSION_ID_HEADER_NAME + " not present on a conflict response");
        }

        return newSessionId;
    }
}
