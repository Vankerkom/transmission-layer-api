package be.vankerkom.transmissionlayer.models.dto.partials;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransmissionTorrentDataDto {

    private Integer id;

    private String name;

    private String hashString;

}
