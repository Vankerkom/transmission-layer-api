package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TransmissionAddTorrentDto {

    @JsonProperty("torrent-added")
    private TransmissionTorrentDataDto torrentAdded;

    @JsonProperty("torrent-duplicate")
    private TransmissionTorrentDataDto duplicate;

}
