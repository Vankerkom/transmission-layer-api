package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.NewTorrentRequest;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDataDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import be.vankerkom.transmissionlayer.repositories.TorrentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TorrentServiceImpl implements TorrentService {

    private final Logger LOG = LogManager.getLogger(getClass());

    @Autowired
    private TransmissionService transmissionService;

    @Autowired
    private TorrentRepository torrentRepository;

    @Autowired
    private ModelMapper mapper;

    public Optional<List<TorrentDto>> getTorrents(final UserPrincipal userPrincipal) {
        final List<Torrent> torrents = torrentRepository.findByUser(userPrincipal.getUser());

        if (torrents.isEmpty()) {
            return Optional.of(Collections.emptyList());
        }

        final List<String> fields = Arrays.asList("id", "name", "status", "hashString", "percentDone");
        final Set<Integer> ids = mapTorrentsToIdSet(torrents);

        return Optional.ofNullable(transmissionService.getTorrents(fields, ids));
    }

    @Override
    public Optional<TorrentDto> addTorrent(final UserPrincipal userPrincipal, final NewTorrentRequest request) throws DuplicateException {
        final User user = userPrincipal.getUser();

        // Add the torrent to Transmission.
        final Optional<TorrentDataDto> result = transmissionService.addTorrent(request);

        if (!result.isPresent()) {
            LOG.error("Failed to add a new torrent by user: {}, fileName: {}", user.getUsername(), request.getFileName());
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

        LOG.debug("{} added a new torrent with id: {}", user.getUsername(), torrent.getId());

        final TorrentDto torrentDto = mapper.map(torrentData, TorrentDto.class);

        return Optional.ofNullable(torrentDto);
    }

    private Optional<Torrent> attachTorrentToUser(final TorrentDataDto torrentData, final User user) {
        final int torrentId = torrentData.getId();

        final Torrent newTorrent = new Torrent(torrentId, user);

        try {
            return Optional.ofNullable(torrentRepository.save(newTorrent));
        } catch (Exception e) {
            final String userValue = user != null
                    ? user.toString()
                    : "<NULL>";

            LOG.error("Failed to attach torrentId: {} to user: {}", torrentId, userValue, e);
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
