package be.vankerkom.transmissionlayer.models.dto.partials;

public class TorrentDto extends TorrentDataDto {

    private Integer status = 0;

    private Float percentDone = 0.0f;

    private boolean isFinished;

    public Integer getStatus() {
        return status;
    }

    public Float getPercentDone() {
        return percentDone;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean finished) {
        this.isFinished = finished;
    }

}
