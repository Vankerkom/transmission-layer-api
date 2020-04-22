package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class TransmissionTorrentListDto {

    @JsonProperty("torrents")
    private List<TransmissionTorrentDto> torrents;

    @JsonProperty("removed")
    private List<Integer> removed;

}
