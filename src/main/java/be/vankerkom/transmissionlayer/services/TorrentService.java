package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.exceptions.EntityNotFoundException;
import be.vankerkom.transmissionlayer.factory.TorrentFactory;
import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.NewTorrentRequest;
import be.vankerkom.transmissionlayer.models.dto.TorrentState;
import be.vankerkom.transmissionlayer.models.dto.partials.TransmissionTorrentDataDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TransmissionTorrentDto;
import be.vankerkom.transmissionlayer.repositories.TorrentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
@Slf4j
public class TorrentService {

    private static final List<String> GET_TORRENTS_FIELDS = asList(
            "id", "name", "status", "hashString", "percentDone",
            "isFinished", "totalSize", "downloadedEver", "uploadedEver",
            "rateDownload", "rateUpload"
    );

    private static final Set<TorrentState> SEEDING_STATES = EnumSet.of(
            TorrentState.SEEDING, TorrentState.SEEDING_QUEUED
    );

    private static final Set<TorrentState> DOWNLOAD_STATES = EnumSet.of(
            TorrentState.CHECK_QUEUED, TorrentState.CHECKING, TorrentState.DOWNLOAD_QUEUED, TorrentState.DOWNLOADING
    );

    private final TransmissionService transmissionService;
    private final TorrentRepository torrentRepository;

    public List<TransmissionTorrentDto> getTorrents(final UserPrincipal userPrincipal, final String filter) {
        final List<Torrent> torrents = torrentRepository.findByUser(userPrincipal.getUser());

        if (torrents.isEmpty()) {
            return Collections.emptyList();
        }

        final Set<Integer> ids = mapTorrentsToIdSet(torrents);

        return filterData(transmissionService.getTorrents(GET_TORRENTS_FIELDS, ids), filter);
    }

    private List<TransmissionTorrentDto> filterData(final List<TransmissionTorrentDto> torrents, final String filter) {
        Stream<TransmissionTorrentDto> torrentStream = torrents.stream();

        switch (filter.toLowerCase()) {
            case "downloading":
                torrentStream = torrentStream.filter(torrent -> DOWNLOAD_STATES.contains(torrent.getStatus()));
                break;

            case "seeding":
                torrentStream = torrentStream.filter(torrent -> SEEDING_STATES.contains(torrent.getStatus()));
                break;

            case "inactive":
                torrentStream = torrentStream.filter(torrent -> TorrentState.STOPPED.equals(torrent.getStatus()));
                break;

            case "completed":
                torrentStream = torrentStream.filter(TransmissionTorrentDto::isFinished);
                break;

            default:
                break;
        }

        return torrentStream.collect(Collectors.toUnmodifiableList());
    }

    public Optional<TransmissionTorrentDataDto> addTorrent(final UserPrincipal userPrincipal, final NewTorrentRequest request) throws DuplicateException {
        final User user = userPrincipal.getUser();

        request.setDownloadDirectory(user.getDownloadDirectory());

        // Add the torrent to Transmission.
        final TransmissionTorrentDataDto torrentData = transmissionService.addTorrent(request)
                .orElseThrow();

        // Add the new torrent to the database.
        final Optional<Torrent> savedTorrent = attachTorrentToUser(torrentData, user);

        if (!savedTorrent.isPresent()) {
            // If saving the torrent data to the database fails, remove all contents of it from the transmission instance.
            transmissionService.removeTorrent(torrentData.getId(), true);

            return Optional.empty();
        }

        final Torrent torrent = savedTorrent.get();

        // Start torrent after it has been saved.
        transmissionService.startTorrent(torrent.getId());

        log.debug("{} added a new torrent with id: {}", user.getUsername(), torrent.getId());

        return Optional.of(torrentData);
    }

    public void deleteByUserAndId(User user, String hash) {
        final Torrent torrent = torrentRepository.findByUserAndHash(user, hash)
                .orElseThrow(() -> new EntityNotFoundException("Torrent with hash: " + hash + " not found"));

        torrentRepository.delete(torrent);

        transmissionService.removeTorrent(torrent.getId(), false);
    }

    private Optional<Torrent> attachTorrentToUser(final TransmissionTorrentDataDto torrentData, final User user) {
        final Torrent newTorrent = TorrentFactory.create(torrentData, user);

        try {
            return Optional.of(torrentRepository.save(newTorrent));
        } catch (Exception e) {
            log.error("Failed to attach torrent:{} to user: {}", newTorrent, user, e);
            return Optional.empty();
        }
    }

    private static Set<Integer> mapTorrentsToIdSet(final List<Torrent> torrents) {
        return torrents.stream()
                .mapToInt(Torrent::getId)
                .boxed()
                .collect(Collectors.toSet());
    }

}
