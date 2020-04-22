package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TransmissionTorrentDto extends TransmissionTorrentDataDto {

    private Integer status = 0;

    private Float percentDone = 0.0f;

    @JsonProperty("isFinished")
    private boolean finished;

    private Long totalSize;

    private Long downloadedEver;

    private Long uploadedEver;

    private Long rateDownload;

    private Long rateUpload;

}
