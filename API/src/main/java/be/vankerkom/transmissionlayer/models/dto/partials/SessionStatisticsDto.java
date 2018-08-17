package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Integer getActiveTorrentCount() {
        return activeTorrentCount;
    }

    public SessionStatisticsDataDto getCumulative() {
        return cumulative;
    }

    public SessionStatisticsDataDto getCurrent() {
        return current;
    }

    public Long getDownloadSpeed() {
        return downloadSpeed;
    }

    public Long getPausedTorrentCount() {
        return pausedTorrentCount;
    }

    public Long getTorrentCount() {
        return torrentCount;
    }

    public Long getUploadSpeed() {
        return uploadSpeed;
    }

}
