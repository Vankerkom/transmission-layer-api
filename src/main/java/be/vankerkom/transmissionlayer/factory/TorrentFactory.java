package be.vankerkom.transmissionlayer.factory;

import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.models.dto.partials.TransmissionTorrentDataDto;

public final class TorrentFactory {

    public static Torrent create(TransmissionTorrentDataDto dto, User user) {
        final Torrent torrent = new Torrent();

        torrent.setHash(dto.getHashString());
        torrent.setUser(user);
        torrent.setId(dto.getId());

        return torrent;
    }

    private TorrentFactory() {
        throw new IllegalStateException("Factory method class");
    }

}
