package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TorrentDto extends TorrentDataDto {

    private Integer status = 0;

    private Float percentDone = 0.0f;

    @JsonProperty("isFinished")
    private boolean finished;

}
