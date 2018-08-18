package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import be.vankerkom.transmissionlayer.repositories.TorrentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TorrentServiceImpl implements TorrentService {

    @Autowired
    private TransmissionService transmissionService;

    @Autowired
    private TorrentRepository torrentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TorrentDto> getTorrents(UserPrincipal userPrincipal) {
        final List<Torrent> torrents = torrentRepository.findByUser(userPrincipal.getUser());

        if (torrents.isEmpty()) {
            return Collections.emptyList();
        }

        final List<String> fields = Arrays.asList("id", "name", "status", "percentDone");
        final Set<Integer> ids = mapTorrentsToIdSet(torrents);

        return transmissionService.getTorrents(fields, ids);
    }

    private static Set<Integer> mapTorrentsToIdSet(final List<Torrent> torrents) {
        return torrents.stream()
                .mapToInt(Torrent::getId)
                .boxed()
                .collect(Collectors.toSet());
    }

}
