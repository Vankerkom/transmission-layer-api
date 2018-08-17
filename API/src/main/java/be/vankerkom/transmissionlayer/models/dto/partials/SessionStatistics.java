package be.vankerkom.transmissionlayer.models.dto.partials;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionStatistics {

    private Integer activeTorrentCount;

    @JsonProperty("cumulative-stats")
    private SessionStatisticsData cumulative;

    @JsonProperty("current-stats")
    private SessionStatisticsData current;

    private Long downloadSpeed;

    private Long pausedTorrentCount;

    private Long torrentCount;

    private Long uploadSpeed;

    public Integer getActiveTorrentCount() {
        return activeTorrentCount;
    }

    public void setActiveTorrentCount(Integer activeTorrentCount) {
        this.activeTorrentCount = activeTorrentCount;
    }

    public SessionStatisticsData getCumulative() {
        return cumulative;
    }

    public void setCumulative(SessionStatisticsData cumulative) {
        this.cumulative = cumulative;
    }

    public SessionStatisticsData getCurrent() {
        return current;
    }

    public void setCurrent(SessionStatisticsData current) {
        this.current = current;
    }

    public Long getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(Long downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public Long getPausedTorrentCount() {
        return pausedTorrentCount;
    }

    public void setPausedTorrentCount(Long pausedTorrentCount) {
        this.pausedTorrentCount = pausedTorrentCount;
    }

    public Long getTorrentCount() {
        return torrentCount;
    }

    public void setTorrentCount(Long torrentCount) {
        this.torrentCount = torrentCount;
    }

    public Long getUploadSpeed() {
        return uploadSpeed;
    }

    public void setUploadSpeed(Long uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }

}
