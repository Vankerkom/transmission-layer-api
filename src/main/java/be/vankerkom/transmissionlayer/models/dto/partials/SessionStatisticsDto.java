package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SessionStatisticsDto {

    private Integer activeTorrentCount;

    @JsonProperty("cumulative-stats")
    private SessionStatisticsDataDto cumulative;

    @JsonProperty("current-stats")
    private SessionStatisticsDataDto current;

    private Long downloadSpeed;

    private Long pausedTorrentCount;

    private Long torrentCount;

    private Long uploadSpeed;

}
