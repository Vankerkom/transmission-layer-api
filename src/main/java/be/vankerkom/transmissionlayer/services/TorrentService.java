package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.exceptions.EntityNotFoundException;
import be.vankerkom.transmissionlayer.factory.TorrentFactory;
import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.NewTorrentRequest;
import be.vankerkom.transmissionlayer.models.dto.partials.TransmissionTorrentDataDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TransmissionTorrentDto;
import be.vankerkom.transmissionlayer.repositories.TorrentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    private final TransmissionService transmissionService;
    private final TorrentRepository torrentRepository;
    private final ModelMapper mapper;

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
                torrentStream = torrentStream.filter(torrent -> torrent.getStatus() == 1 || torrent.getStatus() == 4);
                break;

            case "seeding":
                torrentStream = torrentStream.filter(torrent -> torrent.getStatus() == 6);
                break;

            case "inactive":
                torrentStream = torrentStream.filter(torrent -> torrent.getStatus() == 0);
                break;

            case "completed":
                torrentStream = torrentStream.filter(TransmissionTorrentDto::isFinished);
                break;

            default:
                break;
        }

        return torrentStream.collect(Collectors.toUnmodifiableList());
    }

    public Optional<TransmissionTorrentDto> addTorrent(final UserPrincipal userPrincipal, final NewTorrentRequest request) throws DuplicateException {
        final User user = userPrincipal.getUser();

        request.setDownloadDirectory(user.getDownloadDirectory());

        // Add the torrent to Transmission.
        final Optional<TransmissionTorrentDataDto> result = transmissionService.addTorrent(request);

        if (!result.isPresent()) {
            log.error("Failed to add a new torrent by user: {}, request: {}", user.getUsername(), request);
            return Optional.empty();
        }

        final TransmissionTorrentDataDto torrentData = result.get();

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

        final TransmissionTorrentDto transmissionTorrentDto = mapper.map(torrentData, TransmissionTorrentDto.class);

        return Optional.ofNullable(transmissionTorrentDto);
    }

    public void deleteByUserAndId(final User user, final int id) {
        final Torrent torrent = torrentRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new EntityNotFoundException("Torrent with id: " + id + " not found"));

        torrentRepository.delete(torrent);

        transmissionService.removeTorrent(torrent.getId(), false);
    }

    private Optional<Torrent> attachTorrentToUser(final TransmissionTorrentDataDto torrentData, final User user) {
        final int torrentId = torrentData.getId();

        final Torrent newTorrent = TorrentFactory.create(torrentId, user);

        try {
            return Optional.of(torrentRepository.save(newTorrent));
        } catch (Exception e) {
            log.error("Failed to attach torrentId: {} to user: {}", torrentId, user, e);
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
