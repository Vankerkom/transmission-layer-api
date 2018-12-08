package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.models.dto.*;
import be.vankerkom.transmissionlayer.models.dto.partials.AddTorrentDto;
import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatisticsDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDataDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import be.vankerkom.transmissionlayer.transmission.TransmissionSessionIdInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TransmissionServiceImpl implements TransmissionService {

    private static final Logger LOG = LogManager.getLogger(TransmissionServiceImpl.class);

    @Value("${transmission.url}")
    private String resourceHost;

    private RestTemplate restTemplate;

    private final ModelMapper mapper;

    @Autowired
    public TransmissionServiceImpl(@Value("${transmission.username}") final String username,
                                   @Value("${transmission.password}") final String password,
                                   final RestTemplateBuilder restTemplateBuilder,
                                   final ModelMapper mapper) {

        restTemplate = restTemplateBuilder
                .setConnectTimeout(1000)
                .setReadTimeout(1000)
                .interceptors(new TransmissionSessionIdInterceptor())
                .basicAuthorization(username, password)
                .build();

        this.mapper = mapper;
    }

    public Optional<Object> getSession() {
        final TransmissionResponseGeneric response = getResource("session-get");

        final Object arguments = isSuccessResponse(response)
                ? response.getArguments()
                : null;

        return Optional.ofNullable(arguments);
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

    public Optional<TorrentDataDto> addTorrent(final NewTorrentRequest torrentRequestData) throws DuplicateException {
        final AddTorrentRequest request = mapper.map(torrentRequestData, AddTorrentRequest.class);

        request.setPaused(true); // Always pause the torrent.

        final TransmissionResponseAddTorrent response = getResource("torrent-add", request, TransmissionResponseAddTorrent.class);
        final AddTorrentDto arguments = response.getArguments();

        if (!isSuccessResponse(response) || arguments == null) {
            return Optional.empty();
        }

        final TorrentDataDto duplicate = arguments.getDuplicate();

        if (duplicate != null) {
            throw new DuplicateException(duplicate);
        }

        return Optional.ofNullable(arguments.getTorrentAdded());
    }

    @Override
    public void removeTorrent(final int id, final boolean deleteLocalContent) {
        removeTorrents(Collections.singleton(id), deleteLocalContent);
    }

    @Override
    public void removeTorrents(final Set<Integer> ids, final  boolean deleteLocalContent) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("Ids cannot be empty");
        }

        // Set-up the request data.
        final RemoveTorrentRequest request = new RemoveTorrentRequest(ids, deleteLocalContent);

        // Execute the API call.
        final TransmissionResponseGeneric response = getResource("torrent-remove", request);

        if (!isSuccessResponse(response)) {
            LOG.error("Failed to delete torrents - deleteLocalContent: {}, ids: {}", deleteLocalContent, ids);
        }
    }

    @Override
    public Optional<SessionStatisticsDto> getSessionStats() {
        final TransmissionResponseSessionStatistics response = getResource("session-stats", TransmissionResponseSessionStatistics.class);

        final SessionStatisticsDto statisticsDto = isSuccessResponse(response)
                ? response.getArguments()
                : null;

        return Optional.ofNullable(statisticsDto);
    }

    public TransmissionResponseGeneric getResource(final String method) {
        return getResource(method, (Map<String, Object>) null);
    }

    public TransmissionResponseGeneric getResource(final String method, final Object arguments) {
        return getResource(method, arguments, TransmissionResponseGeneric.class);
    }

    private <T extends TransmissionResponse<?>> T getResource(final String method, final Class<T> clazz) {
        return getResource(method, null, clazz);
    }

    private <T extends TransmissionResponse<?>> T getResource(final String method, final Object arguments, final Class<T> clazz) {
        final Optional<ResponseEntity<T>> response = getRemoteResource(method, arguments, clazz);

        if (LOG.isDebugEnabled()) {
            response.ifPresent(r -> LOG.debug("Method: {}, Response Status Code: {}", method, r.getStatusCode()));
        }

        return response.map(HttpEntity::getBody)
                .orElse(null);
    }

    private <T extends TransmissionResponse<?>> Optional<ResponseEntity<T>> getRemoteResource(final String method, final Object arguments, final Class<T> clazz) {
        try {
            final ResponseEntity<T> response = restTemplate.postForEntity(
                    resourceHost,
                    new TransmissionRequest(method, arguments),
                    clazz
            );

            return Optional.of(response);
        } catch (Exception e) {
            LOG.error("Failed to fetch remote resource: {}", method, e);
            return Optional.empty();
        }
    }

    private static boolean isSuccessResponse(final TransmissionResponse<?> response) {
        return response != null && response.isSuccess();
    }

}
