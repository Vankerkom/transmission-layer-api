package be.vankerkom.transmissionlayer.models.dto.partials;

public class SessionStatisticsData {

    private Long downloadedBytes;

    private Integer filesAdded;

    private Long secondsActive;

    private Integer sessionCount;

    private Long uploadedBytes;

    public Long getDownloadedBytes() {
        return downloadedBytes;
    }

    public void setDownloadedBytes(Long downloadedBytes) {
        this.downloadedBytes = downloadedBytes;
    }

    public Integer getFilesAdded() {
        return filesAdded;
    }

    public void setFilesAdded(Integer filesAdded) {
        this.filesAdded = filesAdded;
    }

    public Long getSecondsActive() {
        return secondsActive;
    }

    public void setSecondsActive(Long secondsActive) {
        this.secondsActive = secondsActive;
    }

    public Integer getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(Integer sessionCount) {
        this.sessionCount = sessionCount;
    }

    public Long getUploadedBytes() {
        return uploadedBytes;
    }

    public void setUploadedBytes(Long uploadedBytes) {
        this.uploadedBytes = uploadedBytes;
    }

}
