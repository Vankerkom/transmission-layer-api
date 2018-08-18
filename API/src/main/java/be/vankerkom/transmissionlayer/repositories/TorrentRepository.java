package be.vankerkom.transmissionlayer.repositories;

import be.vankerkom.transmissionlayer.models.Torrent;
import org.springframework.data.repository.CrudRepository;

public interface TorrentRepository extends CrudRepository<Torrent, Integer> {
}
