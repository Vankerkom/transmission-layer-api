package be.vankerkom.transmissionlayer.models.dto.partials;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransmissionTorrentDataDto {

    private Integer id;

    private String name;

    private String hashString;

}
