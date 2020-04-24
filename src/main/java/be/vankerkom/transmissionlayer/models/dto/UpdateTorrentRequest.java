package be.vankerkom.transmissionlayer.models.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UpdateTorrentRequest {

    private TorrentState state;

}
