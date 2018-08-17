package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TorrentListDto {

    @JsonProperty("torrents")
    private List<TorrentDto> torrents;

    @JsonProperty("removed")
    private List<Integer> removed;

    public List<TorrentDto> getTorrents() {
        return torrents;
    }

    public List<Integer> getRemoved() {
        return removed;
    }

}
