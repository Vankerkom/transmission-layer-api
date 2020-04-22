package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TransmissionSessionStatisticsDto {

    private Integer activeTorrentCount;

    @JsonProperty("cumulative-stats")
    private TransmissionSessionStatisticsDataDto cumulative;

    @JsonProperty("current-stats")
    private TransmissionSessionStatisticsDataDto current;

    private Long downloadSpeed;

    private Long pausedTorrentCount;

    private Long torrentCount;

    private Long uploadSpeed;

}
