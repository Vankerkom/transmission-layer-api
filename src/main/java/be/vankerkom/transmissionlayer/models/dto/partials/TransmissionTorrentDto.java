package be.vankerkom.transmissionlayer.models.dto.partials;

import be.vankerkom.transmissionlayer.models.dto.TorrentState;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TransmissionTorrentDto extends TransmissionTorrentDataDto {

    private TorrentState status = TorrentState.STOPPED;

    private Float percentDone = 0.0f;

    @JsonProperty("isFinished")
    private boolean finished;

    private Long totalSize;

    private Long downloadedEver;

    private Long uploadedEver;

    private Long rateDownload;

    private Long rateUpload;

}
