package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AddTorrentDto {

    @JsonProperty("torrent-added")
    private TorrentDataDto torrentAdded;

    @JsonProperty("torrent-duplicate")
    private TorrentDataDto duplicate;

}
