package be.vankerkom.transmissionlayer.models.dto.partials;

public class TorrentDto extends TorrentDataDto {

    private Integer status = 0;

    private Float percentDone = 0.0f;

    public Integer getStatus() {
        return status;
    }

    public Float getPercentDone() {
        return percentDone;
    }

}
