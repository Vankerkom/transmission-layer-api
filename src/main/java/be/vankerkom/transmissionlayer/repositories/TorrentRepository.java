package be.vankerkom.transmissionlayer.repositories;

import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TorrentRepository extends CrudRepository<Torrent, Integer> {

    List<Torrent> findByUser(User user);

    Optional<Torrent> findByUserAndId(User user, int id);

}
