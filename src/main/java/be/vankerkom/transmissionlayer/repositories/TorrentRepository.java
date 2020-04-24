package be.vankerkom.transmissionlayer.repositories;

import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TorrentRepository extends JpaRepository<Torrent, Integer> {

    List<Torrent> findByUser(User user);

    Optional<Torrent> findByUserAndHash(User user, String hash);

}
