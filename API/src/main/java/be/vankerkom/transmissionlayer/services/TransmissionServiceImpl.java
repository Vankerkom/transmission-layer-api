package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.dto.*;
import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatisticsDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import be.vankerkom.transmissionlayer.transmission.TransmissionSessionIdInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public List<TorrentDto> getTorrents(final List<String> fields) {
       return getTorrents(fields, null);
    }

    public List<TorrentDto> getTorrents(final List<String> fields, final Set<Integer> ids) {
        final GetTorrentsRequest request = new GetTorrentsRequest(fields, ids);

        final TransmissionResponseTorrents response = getResource("torrent-get", request, TransmissionResponseTorrents.class);

        if (response.isSuccess()) {
            return response.getArguments().getTorrents();
        }

        return Collections.emptyList();
    }

    public void addTorrent() {
        final AddTorrentRequest request = new AddTorrentRequest();

        final TransmissionResponseGeneric response = getResource("torent-add", request);
    }

    @Override
    public SessionStatisticsDto getSessionStats() {
        final TransmissionResponseSessionStatistics response = getResource("session-stats", TransmissionResponseSessionStatistics.class);

        if (response.isSuccess()) {
            return response.getArguments();
        }

        return null;
    }

    public TransmissionResponseGeneric getResource(final String method) {
        return getResource(method, (Map<String, Object>) null);
    }

    public TransmissionResponseGeneric getResource(final String method, final Object arguments) {
        return getResource(method, arguments, TransmissionResponseGeneric.class);
    }

    private <T extends TransmissionResponse<?>> T getResource(final String method, Class<T> clazz) {
        return getResource(method, null, clazz);
    }

    private <T extends TransmissionResponse<?>> T getResource(final String method, final Object arguments, Class<T> clazz) {
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
