package be.vankerkom.transmissionlayer.factory;

import be.vankerkom.transmissionlayer.models.Torrent;
import be.vankerkom.transmissionlayer.models.User;

public final class TorrentFactory {

    public static Torrent create(final int id, final User user) {
        final Torrent torrent = new Torrent();

        torrent.setId(id);
        torrent.setUser(user);

        return torrent;
    }

    private TorrentFactory() {
        throw new IllegalStateException("Factory method class");
    }

}
