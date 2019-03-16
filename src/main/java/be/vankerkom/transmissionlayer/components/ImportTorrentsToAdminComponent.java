package be.vankerkom.transmissionlayer.components;

import be.vankerkom.transmissionlayer.config.AdminConfigurationProperties;
import be.vankerkom.transmissionlayer.factory.TorrentFactory;
import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import be.vankerkom.transmissionlayer.repositories.TorrentRepository;
import be.vankerkom.transmissionlayer.repositories.UserRepository;
import be.vankerkom.transmissionlayer.services.TransmissionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Component
@Order(2)
public class ImportTorrentsToAdminComponent implements ApplicationRunner {

    private final Logger LOG = LogManager.getLogger(getClass());

    @Autowired
    private AdminConfigurationProperties adminConfigurationProperties;

    @Autowired
    private TransmissionService transmissionService;

    @Autowired
    private TorrentRepository torrentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!adminConfigurationProperties.isEnabled()) {
            return;
        }

        if (torrentRepository.count() > 0) {
            return;
        }

        final Optional<User> user = userRepository.findById(1);

        user.ifPresent(this::importTorrents);
    }

    private void importTorrents(final User user) {
        if (!user.getTorrents().isEmpty()) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Importing all torrents to the administrator account...");
        }

        final List<Torrent> importedTorrents = getAndMapImportedTorrents(user);

        if (importedTorrents.isEmpty()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No torrents to import.");
            }
            return;
        }

        final Long savedTorrentsCount = torrentRepository.saveAll(importedTorrents).spliterator().getExactSizeIfKnown();

        userRepository.save(user);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Imported {} torrents.", savedTorrentsCount);
        }

    }

    private List<Torrent> getAndMapImportedTorrents(final User user) {
        final List<TorrentDto> torrents = transmissionService.getTorrents(Collections.singletonList("id"));

        return torrents.stream()
                .map(t -> TorrentFactory.create(t.getId(), user))
                .collect(Collectors.toList());
    }

}
