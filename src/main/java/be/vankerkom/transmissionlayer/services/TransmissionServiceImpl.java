package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.models.dto.*;
import be.vankerkom.transmissionlayer.models.dto.partials.AddTorrentDto;
import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatisticsDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDataDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import be.vankerkom.transmissionlayer.transmission.TorrentActionRequest;
import be.vankerkom.transmissionlayer.transmission.TransmissionSessionIdInterceptor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class TransmissionServiceImpl implements TransmissionService {

    @Value("${transmission.url}")
    private String resourceHost;

    private final RestTemplate restTemplate;

    private final ModelMapper mapper;

    @Autowired
    public TransmissionServiceImpl(@Value("${transmission.username}") final String username,
                                   @Value("${transmission.password}") final String password,
                                   final RestTemplateBuilder restTemplateBuilder,
                                   final ModelMapper mapper) {

        restTemplate = restTemplateBuilder
                .interceptors(new TransmissionSessionIdInterceptor())
                .basicAuthentication(username, password)
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

        if (isSuccessResponse(response)) {
            return response.getArguments().getTorrents();
        }

        return Collections.emptyList();
    }

    public Optional<TorrentDataDto> addTorrent(final NewTorrentRequest torrentRequestData) throws DuplicateException {
        final AddTorrentRequest request = new AddTorrentRequest();

        if (torrentRequestData.isUrl()) {
            // TODO Validate if the data is a valid torrent magnet link, else let transmission handle it.
            request.setFileName(torrentRequestData.getData());
        }else {
            // TODO Validate if the data is base64.
            request.setMetaInfo(torrentRequestData.getData());
        }

        request.setPaused(true); // Always pause the torrent, it will be started if it's successfully stored in the db.

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
            log.error("Failed to delete torrents - deleteLocalContent: {}, ids: {}", deleteLocalContent, ids);
        }
    }

    @Override
    public void startTorrent(final int id) {
        startTorrents(Collections.singleton(id));
    }

    @Override
    public void startTorrents(final Set<Integer> ids) {
        sendActionRequest(TorrentActionRequest.START, ids);
    }

    @Override
    public void stopTorrent(final int id) {
        stopTorrents(Collections.singleton(id));
    }

    @Override
    public void stopTorrents(final Set<Integer> ids) {
        sendActionRequest(TorrentActionRequest.STOP, ids);
    }

    private void sendActionRequest(final TorrentActionRequest action, final Set<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("Ids cannot be empty");
        }

        final String actionMethodName = action.getMethodName();

        // Set-up the request data.
        final IdsRequest request = new IdsRequest(ids);

        // Execute the API call.
        final TransmissionResponseGeneric response = getResource(actionMethodName, request);

        if (!isSuccessResponse(response)) {
            log.error("Failed to {} torrents - ids: {}", action, ids);
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

        if (log.isDebugEnabled()) {
            response.ifPresent(r -> log.debug("Method: {}, Response Status Code: {}", method, r.getStatusCode()));
        }

        return response.map(HttpEntity::getBody)
                .orElseThrow(() -> new RuntimeException("Could not communicate with the Transmission RPC"));
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
            log.error("Failed to fetch remote resource: {}", method, e);
            return Optional.empty();
        }
    }

    private static boolean isSuccessResponse(final TransmissionResponse<?> response) {
        return response != null && response.isSuccess();
    }

}
