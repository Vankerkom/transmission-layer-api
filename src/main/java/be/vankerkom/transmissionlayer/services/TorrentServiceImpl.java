package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.factory.TorrentFactory;
import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.NewTorrentRequest;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDataDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import be.vankerkom.transmissionlayer.repositories.TorrentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class TorrentServiceImpl implements TorrentService {

    private final TransmissionService transmissionService;

    private final TorrentRepository torrentRepository;

    private final ModelMapper mapper;

    public List<TorrentDto> getTorrents(final UserPrincipal userPrincipal, final String filter) {
        final List<Torrent> torrents = torrentRepository.findByUser(userPrincipal.getUser());

        if (torrents.isEmpty()) {
            return Collections.emptyList();
        }

        final List<String> fields = Arrays.asList("id", "name", "status", "hashString", "percentDone", "isFinished");
        final Set<Integer> ids = mapTorrentsToIdSet(torrents);

        return filterData(transmissionService.getTorrents(fields, ids), filter);
    }

    private List<TorrentDto> filterData(final List<TorrentDto> torrents, final String filter) {
        Stream<TorrentDto> torrentStream = torrents.stream();

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
                torrentStream = torrentStream.filter(TorrentDto::isFinished);
                break;

            default:
                break;
        }

        return torrentStream.collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<TorrentDto> addTorrent(final UserPrincipal userPrincipal, final NewTorrentRequest request) throws DuplicateException {
        final User user = userPrincipal.getUser();

        // Add the torrent to Transmission.
        final Optional<TorrentDataDto> result = transmissionService.addTorrent(request);

        if (!result.isPresent()) {
            log.error("Failed to add a new torrent by user: {}, fileName: {}", user.getUsername(), request.getFileName());
            return Optional.empty();
        }

        final TorrentDataDto torrentData = result.get();

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

        final TorrentDto torrentDto = mapper.map(torrentData, TorrentDto.class);

        return Optional.ofNullable(torrentDto);
    }

    private Optional<Torrent> attachTorrentToUser(final TorrentDataDto torrentData, final User user) {
        final int torrentId = torrentData.getId();

        final Torrent newTorrent = TorrentFactory.create(torrentId, user);

        try {
            return Optional.of(torrentRepository.save(newTorrent));
        } catch (Exception e) {
            log.error("Failed to attach torrentId: {} to user: {}", torrentId, user, e);
        }

        return Optional.empty();
    }

    private static Set<Integer> mapTorrentsToIdSet(final List<Torrent> torrents) {
        return torrents.stream()
                .mapToInt(Torrent::getId)
                .boxed()
                .collect(Collectors.toSet());
    }

}
