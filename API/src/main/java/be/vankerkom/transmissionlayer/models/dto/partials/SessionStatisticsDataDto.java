package be.vankerkom.transmissionlayer.models.dto.partials;

public class SessionStatisticsDataDto {

    private Long downloadedBytes;

    private Integer filesAdded;

    private Long secondsActive;

    private Integer sessionCount;

    private Long uploadedBytes;

    public Long getDownloadedBytes() {
        return downloadedBytes;
    }

    public Integer getFilesAdded() {
        return filesAdded;
    }

    public Long getSecondsActive() {
        return secondsActive;
    }

    public Integer getSessionCount() {
        return sessionCount;
    }

    public Long getUploadedBytes() {
        return uploadedBytes;
    }

}
