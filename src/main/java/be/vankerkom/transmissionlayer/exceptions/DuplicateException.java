package be.vankerkom.transmissionlayer.exceptions;

import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDataDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateException extends Exception {

    public DuplicateException(final String message) {
        super(message);
    }

    public DuplicateException(TorrentDataDto torrentData) {
        super(String.format(
                "Duplicate torrent (torrentId: %d, torrentName: %s, hash: %s)",
                torrentData.getId(),
                torrentData.getName(),
                torrentData.getHashString()
        ));
    }

}
