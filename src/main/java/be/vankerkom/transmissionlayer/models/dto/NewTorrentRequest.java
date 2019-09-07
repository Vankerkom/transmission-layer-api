package be.vankerkom.transmissionlayer.models.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NewTorrentRequest {

    public boolean url;

    private String data;

}
