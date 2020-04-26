package be.vankerkom.transmissionlayer.components;

import be.vankerkom.transmissionlayer.config.AdminConfigurationProperties;
import be.vankerkom.transmissionlayer.factory.TorrentFactory;
import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.repositories.TorrentRepository;
import be.vankerkom.transmissionlayer.repositories.UserRepository;
import be.vankerkom.transmissionlayer.services.TransmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional
@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class ImportTorrentsToAdminComponent implements ApplicationRunner {

    private final AdminConfigurationProperties adminConfigurationProperties;

    private final TransmissionService transmissionService;
    private final TorrentRepository torrentRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (!adminConfigurationProperties.isEnabled() || !adminConfigurationProperties.isImportTorrents() || torrentRepository.count() > 0) {
            return;
        }

        userRepository.findById(1)
                .ifPresent(this::importTorrents);
    }

    private void importTorrents(final User user) {
        if (!user.getTorrents().isEmpty()) {
            return;
        }

        log.debug("Importing all torrents to the administrator account...");

        final List<Torrent> importedTorrents = getAndMapImportedTorrents(user);

        if (importedTorrents.isEmpty()) {
            log.debug("No torrents to import.");
            return;
        }

        final Long savedTorrentsCount = torrentRepository.saveAll(importedTorrents)
                .spliterator()
                .getExactSizeIfKnown();

        userRepository.save(user);

        log.debug("Imported {} torrents.", savedTorrentsCount);
    }

    private List<Torrent> getAndMapImportedTorrents(User user) {
        return transmissionService.getTorrents(List.of("id", "hashString"))
                .stream()
                .map(torrent -> TorrentFactory.create(torrent, user))
                .collect(toList());
    }

}
