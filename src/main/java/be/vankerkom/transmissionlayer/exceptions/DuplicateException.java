package be.vankerkom.transmissionlayer.exceptions;

import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDataDto;

public class DuplicateException extends Exception {

    public DuplicateException(TorrentDataDto torrentData) {
        super(String.format(
                "Duplicate torrent (torrentId: %d, torrentName: %s, hash: %s)",
                torrentData.getId(),
                torrentData.getName(),
                torrentData.getHashString()
        ));
    }

}
