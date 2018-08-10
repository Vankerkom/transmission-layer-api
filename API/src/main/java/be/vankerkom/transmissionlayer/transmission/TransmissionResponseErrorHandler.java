package be.vankerkom.transmissionlayer.transmission;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

public class TransmissionResponseErrorHandler implements ResponseErrorHandler {

    private static final Logger LOG = LogManager.getLogger(TransmissionResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        LOG.error("Error:  {}", clientHttpResponse.getStatusText());
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        LOG.error("Error:  url: {}, Method: {}, Status: {}", url, method, response.getStatusText());
    }

}
