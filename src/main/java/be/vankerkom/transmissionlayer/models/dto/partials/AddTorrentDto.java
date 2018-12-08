package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddTorrentDto {

    @JsonProperty("torrent-added")
    private TorrentDataDto torrentAdded;

    @JsonProperty("torrent-duplicate")
    private TorrentDataDto duplicate;

    public TorrentDataDto getTorrentAdded() {
        return torrentAdded;
    }

    public TorrentDataDto getDuplicate() {
        return duplicate;
    }

}
