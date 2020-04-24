package be.vankerkom.transmissionlayer.components;

import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.dto.partials.TransmissionTorrentDataDto;
import be.vankerkom.transmissionlayer.repositories.TorrentRepository;
import be.vankerkom.transmissionlayer.services.TransmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableMap;

@RequiredArgsConstructor
@Component
@Order(3)
@Log4j2
public class UpdateTorrentsIdsTask {

    private static final int MAX_UPDATES_PER_PAGE = 100;

    private final TransmissionService transmissionService;
    private final TorrentRepository torrentRepository;

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 30 * 1000)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTorrentIdsTask() {
        final var hashIdMap = transmissionService.getTorrents(List.of("id", "hashString"))
                .stream()
                .collect(toUnmodifiableMap(TransmissionTorrentDataDto::getHashString, TransmissionTorrentDataDto::getId));

        setTorrentIds(hashIdMap);
    }

    public void setTorrentIds(Map<String, Integer> hashIdMaps) {
        Pageable pageRequest = PageRequest.of(0, MAX_UPDATES_PER_PAGE);
        var page = torrentRepository.findAll(pageRequest);

        while (!page.isEmpty()) {
            updateTorrentIdsOfPage(page, hashIdMaps);

            pageRequest = pageRequest.next();
            page = torrentRepository.findAll(pageRequest);
        }
    }

    private void updateTorrentIdsOfPage(Page<Torrent> page, Map<String, Integer> hashIdMaps) {
        final var updatedTorrents = page.getContent()
                .stream()
                .map(torrent -> updateTorrentIdOrSetZero(torrent, hashIdMaps)).collect(toList());

        torrentRepository.saveAll(updatedTorrents);
    }

    private static Torrent updateTorrentIdOrSetZero(Torrent torrent, Map<String, Integer> hashIdMaps) {
        final var newTorrentId = hashIdMaps.getOrDefault(torrent.getHash(), 0);
        torrent.setId(newTorrentId);
        return torrent;
    }

}
